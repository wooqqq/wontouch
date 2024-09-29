import React, { useEffect } from 'react';
import lock from '../../assets/icon/lock.png';

interface ModalProps {
  houseNum: number | null;
  closeModal: () => void;
}

const InteractionModal: React.FC<ModalProps> = ({ houseNum, closeModal }) => {

  const handleKeyDown = (event: KeyboardEvent) => {
    if (event.key === 'Escape') {
      closeModal();
    }
  };

  useEffect(() => {
    // 이벤트 등록
    window.addEventListener('keydown', handleKeyDown);

    // 모달 닫히면 이벤트 제거
    return () => {
      window.removeEventListener('keydown', handleKeyDown);
    }
  }, []);

  if (houseNum === null) return null;

  return (
    <>
      {/* 모달 뒤에 어두운 배경 */}
      <div
        className="fixed inset-0 bg-black bg-opacity-50 z-1000"
        onClick={closeModal}
      ></div>

      {/* 모달 창 */}
      <div className="fixed inset-0 flex items-center justify-center z-50">
        <div className="bg-white p-6 rounded-lg shadow-lg w-[500px] h-auto relative">

          {/* 상단 영역 */}
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-xl font-bold">
              {houseNum === 0 ? '거래소' : `${houseNum}번 집`}
            </h2>
            <button
              className="text-gray-600 hover:text-gray-800"
              onClick={closeModal}
            >
              나가기
            </button>
          </div>

          {/* 거래소 모달 */}
          {houseNum !== 0 ? (
            <>
              {/* 이미지와 아이템 정보 섹션 */}
              <div className="flex mb-4">
                <div className="w-1/3 flex items-center justify-center">
                  {/* 이미지 배치 (예시 이미지) */}
                  <img src="path_to_image" alt="아이템 이미지" className="w-16 h-16" />
                </div>
                <div className="w-2/3">
                  {/* 아이템 정보 */}
                  <p className="text-lg font-semibold">사과</p>
                  <p className="text-sm text-gray-600">남은 수량: 50 상자</p>
                  <p className="text-lg font-bold text-yellow-500">50,000 코인</p>
                </div>
              </div>

              {/* 구매/판매 섹션 */}
              <div className="mt-4 flex justify-between items-center">
                <button className="bg-blue-500 text-white py-1 px-3 rounded">매도</button>
                <div className="flex items-center space-x-2">
                  <button className="bg-gray-300 text-gray-600 py-1 px-3 rounded">-</button>
                  <p>0</p>
                  <button className="bg-gray-300 text-gray-600 py-1 px-3 rounded">+</button>
                </div>
                <button className="bg-red-500 text-white py-1 px-3 rounded">매수</button>
              </div>

              {/* 가격 정보 및 텍스트 설명 */}
              <div className="mt-4 p-4 bg-red-100 rounded-lg">
                <p className="text-red-600 font-semibold">전날에 비해 5% 상승</p>
              </div>

              {/* 하단 설명 */}
              <div className="mt-4">
                <p className="text-sm text-gray-700">어서오시게! 무엇을 살텐가? 수량은 제한되어 있으니 빨리 사야 할거야~</p>
              </div>
            </>
          ) : (
            <>
              {/* 마을 뉴스 모달 */}
              <p className="text-yellow-600 font-semibold text-center mb-4">
                랜덤 100G / 마을 당 500G
              </p>
              <div className="space-y-4">
                {/* 뉴스 항목 */}
                {['A마을', 'B마을', 'C마을', 'D마을'].map((village, index) => (
                  <div key={index} className="flex justify-between items-center p-2 bg-gray-200 rounded">
                    <span className="font-bold">News</span>
                    <span>{village}</span>
                    <img src={lock} alt="news" className="w-6 h-6" />
                  </div>
                ))}
              </div>

              {/* 하단 설명 */}
              <div className="mt-4">
                <p className="text-sm text-gray-700">어서오시게! 어느 마을의 소식이 궁금한가?</p>
              </div>
            </>
          )}

        </div>
      </div>
    </>
  );
};

export default InteractionModal;
