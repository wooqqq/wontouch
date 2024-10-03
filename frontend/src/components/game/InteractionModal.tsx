import React, { useEffect, useState } from 'react';
import lock from '../../assets/icon/lock.png';
import board from '../../assets/game/board.png';
import npc from '../../assets/background/npc.png';
import apple from '../../assets/crops/apple.png';
import up from '../../assets/icon/arrow_up.png';
import cancle from '../../assets/icon/cancel.png';
import confirm from '../../assets/icon/confirm.png';

interface ModalProps {
  houseNum: number | null;
  closeModal: () => void;
}

const InteractionModal: React.FC<ModalProps> = ({ houseNum, closeModal }) => {
  const [count, setCount] = useState(0);
  const [purchaseModal, setPurchaseModal] = useState(false);

  const openPurchaseModal = () => {
    setPurchaseModal(true);
  };

  const closePurchaseModal = () => {
    setPurchaseModal(false);
  };

  // const closePurchaseModal = () => {
  //   setPurchaseModal(false);
  // }

  const minusCount = () => {
    if (count > 0) {
      setCount(count - 1);
    }
  };

  const plusCount = () => {
    setCount(count + 1);
  };

  const clearCount = () => {
    setCount(0);
  };
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
    };
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
        {/* 거래소가 아닐 때 */}
        {houseNum !== 0 ? (
          <div className="relative p-6 rounded-lg shadow-lg w-[60%] h-auto z-30 bg-transparent mb-[10%]">
            {/* 보드 이미지 */}
            <img
              src={board}
              alt="보드 이미지"
              className="absolute top-0 left-[25%] w-full h-full z-10"
            />

            {/* 아이템 정보 (보드 위에 텍스트 배치) */}
            <div className="relative z-20 p-6 left-[25%] ml-4">
              <div className="flex justify-end items-center mb-4">
                <button
                  className="text-gray-600 hover:text-gray-800"
                  onClick={closeModal}
                >
                  나가기
                </button>
              </div>

              {/* 아이템 정보 */}
              <div className="flex items-center justify-start mb-[5%]">
                <img
                  src={apple}
                  alt="아이템 이미지"
                  className="w-[20%] h-[20%] border border-black p-8 rounded-md"
                />
                <div className="ml-[5%]">
                  <p className="text-[36px] font-semibold">사과</p>
                  <p className="text-2xl text-gray-600">남은 수량: 50 상자</p>
                  <p className="text-[32px] font-bold text-yellow-500">
                    50,000 코인
                  </p>
                </div>
              </div>

              <div>
                {/* 매수/매도 버튼 */}
                <div>
                  <div className="flex justify-between">
                    <div className="flex-col">
                      <div className="flex items-center space-x-2 justify-between ml-1">
                        <button
                          className="bg-gray-300 text-gray-600 px-3 rounded-full text-[32px]"
                          onClick={minusCount}
                        >
                          -
                        </button>
                        <p className="text-[32px] ml-3 mr-3">{count}</p>
                        <button
                          className="bg-gray-300 text-gray-600 px-3 rounded-full text-[32px]"
                          onClick={plusCount}
                        >
                          +
                        </button>
                      </div>
                      <div className="mt-4 flex items-center ml-1 mr-auto w-full">
                        <button className="bg-blue-500 text-white py-1 px-3 rounded-2xl">
                          매도
                        </button>
                        <button
                          className="bg-gray-500 text-white py-1 px-3 rounded-2xl ml-1"
                          onClick={clearCount}
                        >
                          초기화
                        </button>
                        <button className="bg-red-500 text-white py-1 px-3 rounded-2xl ml-1">
                          매수
                        </button>
                      </div>
                    </div>
                    {/* 가격 변화 */}
                    <div className="flex mt-4 px-2 py-4 bg-red-600 rounded-lg ml-[10%] w-[60%] items-center justify-center">
                      <p className="text-white font-semibold text-[24px] text-end">
                        전날에 비해
                        <span className="semi-bold text-[36px]">5% 상승</span>
                      </p>
                      <img src={up} className="ml-5 w-[60px] h-[60px]" />
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        ) : (
          <div className="relative p-6 rounded-lg shadow-lg w-[60%] h-auto z-30 bg-transparent mb-[10%]">
            {/* 보드 이미지 */}
            <img
              src={board}
              alt="보드 이미지"
              className="absolute top-0 left-[25%] w-full h-full z-10"
            />

            {/* 아이템 정보 (보드 위에 텍스트 배치) */}
            <div className="relative z-20 p-6 left-[25%] ml-4">
              <div className="flex justify-end items-center mb-4">
                <button
                  className="text-gray-600 hover:text-gray-800"
                  onClick={closeModal}
                >
                  나가기
                </button>
              </div>

              <div className="relative bg-white p-6 rounded-lg shadow-lg w-[80%] h-auto z-30 ml-auto mr-auto">
                <p className="text-yellow-600 font-semibold text-center mb-4">
                  랜덤 100G / 마을 당 500G
                </p>
                <div className="space-y-4">
                  {['A마을', 'B마을', 'C마을', 'D마을'].map(
                    (village, index) => (
                      <button
                        key={index}
                        className="flex justify-between items-center p-2 bg-gray-200 rounded w-full"
                        onClick={openPurchaseModal}
                      >
                        <span className="font-bold">News</span>
                        <span>{village}</span>
                        <img src={lock} alt="news" className="w-6 h-6" />
                      </button>
                    ),
                  )}
                </div>
              </div>
            </div>
          </div>
        )}
      </div>

      {/* NPC 이미지 */}
      <div className="fixed top-[75%] z-40 flex items-end w-[95%] h-[20%] shadow-lg">
        <div className="flex-col">
          <p className="text-white text-[36px] text-center ml-[15%]">
            {houseNum === 0 ? '거래소' : `${houseNum}번 마을 상점`}
          </p>
          <img
            src={npc}
            alt="마을 상인"
            className="w-[460px] h-[560px] ml-[10%]"
          />
        </div>
        <div className="bg-[#f4e2c7] p-4 rounded-lg flex flex-col items-start w-full h-full">
          <div className="font-bold text-lg border border-amber-900 rounded-2xl p-2 right-2">
            {houseNum === 0 ? '거래소 상인' : '마을 상인'}
          </div>
          <div className="text-md mt-7 w-full ml-[3%]">
            {houseNum === 0
              ? '어서오시게! 어느 마을의 소식이 궁금한가?'
              : '어서오시게! 무엇을 살텐가? 수량은 제한되어 있으니 빨리 사야 할거야~'}
          </div>
        </div>
      </div>

      {purchaseModal && (
        <div className="fixed inset-0 flex items-center justify-center z-50">
          <div className="relative p-6 rounded-lg shadow-lg w-[60%] h-auto bg-white z-30">
            {/* 보드 이미지 */}
            <img
              src={board}
              alt="보드 이미지"
              className="absolute top-0 left-0 w-full h-full z-10"
            />

            {/* 아이템 정보 (보드 위에 텍스트 배치) */}
            <div className="relative z-20 p-6">
              <div className="flex justify-end items-center mb-4">

                <button
                  onClick={closePurchaseModal}
                >
                  <img src={cancle} alt='아이이잉'/>
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default InteractionModal;
