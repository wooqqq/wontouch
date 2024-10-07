import React, { useEffect, useState } from 'react';
import lock from '../../assets/icon/lock.png';
import board from '../../assets/game/board.png';
import npc from '../../assets/background/npc.png';
import up from '../../assets/icon/arrow_up.png';
import cancle from '../../assets/icon/cancel.png';
//import confirm from '../../assets/icon/confirm.png';
import leftArrow from '../../assets/icon/arrow_left.png'; // 좌측 화살표 이미지
import rightArrow from '../../assets/icon/arrow_right.png'; // 우측 화살표 이미지

interface CropList {
  [cropName: string]: number;
}

interface ModalProps {
  houseNum: number | null;
  closeModal: () => void;
  gameSocket: WebSocket | null;
  cropList?: CropList;
}

// 이미지 파일을 동적으로 가져오기
const cropImages = import.meta.glob('../../assets/crops/*.png');

const InteractionModal: React.FC<ModalProps> = ({ houseNum, closeModal, gameSocket, cropList }) => {
  const [count, setCount] = useState(0);
  const [purchaseModal, setPurchaseModal] = useState(false);

  //작물 인덱스 보기
  const [currentCropIndex, setCurrentCropIndex] = useState(0);
  const [currentImageSrc, setCurrentImageSrc] = useState<string | null>(null);
  // cropList 배열 생성 (Object.entries를 사용하여 객체를 배열로 변환)
  const crops = Object?.entries(cropList ?? {});

  const handlePrev = () => {
    if (currentCropIndex === 0) {
      // 첫 번째 작물에서 이전으로 가려면 마지막 작물로 이동
      setCurrentCropIndex(crops.length - 1);
    } else {
      // 그렇지 않으면 이전 작물로 이동
      setCurrentCropIndex(currentCropIndex - 1);
    }
  };

  const handleNext = () => {
    if (currentCropIndex === crops.length - 1) {
      // 마지막 작물에서 다음으로 가려면 첫 번째 작물로 이동
      setCurrentCropIndex(0);
    } else {
      // 그렇지 않으면 다음 작물로 이동
      setCurrentCropIndex(currentCropIndex + 1);
    }
  };

  const openPurchaseModal = () => {
    setPurchaseModal(true);
  };

  const closePurchaseModal = () => {
    setPurchaseModal(false);
  };

  // const closePurchaseModal = () => {
  //   setPurchaseModal(false);
  // }

  useEffect(() => {
    if (crops.length > 0) {
      const [cropName] = crops[currentCropIndex];

      // 이미지 경로 동적으로 설정
      const loadImage = async () => {
        const imagePath = cropImages[`../../assets/crops/${cropName}.png`];
        if (imagePath) {
          const imageModule = await (imagePath as () => Promise<{ default: string }>)();
          setCurrentImageSrc(imageModule.default);
        }
      };

      loadImage();
    }
  }, [currentCropIndex, crops]);


  useEffect(() => {
    if (houseNum !== null && houseNum !== 0 && gameSocket) {
      let townName = '';
      switch (houseNum) {
        case 1:
          townName = 'DOMESTIC_FRUITS';
          break;
        case 2:
          townName = "VEGETABLES_1";
          break;
        case 3:
          townName = "MUSHROOMS_MEDICINAL";
          break;
        case 4:
          townName = "IMPORTED_FRUITS";
          break;
        case 5:
          townName = "VEGETABLES_2";
          break;
        default:
          townName = "GRAINS_NUTS";
          break;
      }

      // 모달이 열렸을 때, TOWN_CROP_LIST 요청 전송
      const townCropMessage = {
        type: "TOWN_CROP_LIST",
        townName: townName
      };

      gameSocket.send(JSON.stringify(townCropMessage));
      console.log("TOWN_CROP_LIST 요청을 보냈습니다.");
    }
  }, [houseNum, gameSocket]);

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

  if (houseNum === null || crops.length === 0) return null;
  // 현재 선택된 작물 정보 가져오기
  const [cropName, cropAmount] = crops[currentCropIndex];

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

              {/* 작물 정보 */}
              <div className="flex items-center justify-start mb-[5%]">
                <img
                  src={currentImageSrc ?? undefined} // 현재 작물에 맞는 이미지를 선택해야 함
                  alt={`${cropName} 이미지`}
                  className="w-[20%] h-[20%] border border-black p-8 rounded-md"
                />
                <div className="ml-[5%]">
                  <p className="text-[36px] font-semibold">{cropName}</p> {/* 작물 이름 */}
                  <p className="text-2xl text-gray-600">남은 수량: {cropAmount} 상자</p> {/* 남은 수량 */}
                  <p className="text-[32px] font-bold text-yellow-500">50,000 코인</p> {/* 가격은 임의 */}
                </div>
              </div>

              {/* 좌우 화살표 추가 */}
              <div className="flex justify-between items-center">
                <button onClick={handlePrev}>
                  <img src={leftArrow} alt="이전 작물" />
                </button>

                <button onClick={handleNext}>
                  <img src={rightArrow} alt="다음 작물" />
                </button>
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
                  <img src={cancle} alt='아이이잉' />
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
