import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';

interface PlayerCropModalProps {
  onClose: () => void;
  gameSocket: WebSocket | null;
}

const PlayerCropModal: React.FC<PlayerCropModalProps> = ({ onClose, gameSocket }) => {
  const playerCrops = useSelector((state: RootState) => state.playerCrop.crops);
  const allCrops = useSelector((state: RootState) => state.crop.crops); // 전체 작물 정보
  const chart = useSelector((state: RootState) => state.chart); // 차트 데이터를 가져옴
  const [selectedCropId, setSelectedCropId] = useState<string | null>(null);
  const [currentPage, setCurrentPage] = useState<number>(1); // 현재 페이지 상태
  const itemsPerPage = 4; // 페이지당 표시할 아이템 수

  // 0개가 아닌 작물만 필터링
  const purchasedCrops = Object.entries(playerCrops).filter(([cropName, quantity]) => quantity > 0);

  // 총 페이지 수 계산
  const totalPages = Math.ceil(purchasedCrops.length / itemsPerPage);

  // 현재 페이지에 해당하는 아이템들만 슬라이싱해서 보여주기
  const currentCrops = purchasedCrops.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage);

  // cropName에 해당하는 한글 이름 가져오기
  const getCropNameInKorean = (cropName: string) => {
    const crop = allCrops.find((crop) => crop.id === cropName);
    return crop ? crop.name : cropName; // 일치하는 작물의 name을 반환, 없으면 cropName 반환
  };

  // 작물을 클릭했을 때 CROP_CHART 요청 보내기
  const handleCropClick = (cropId: string) => {
    if (gameSocket) {
      const requestData = {
        type: 'CROP_CHART',
        cropId,
      };
      gameSocket.send(JSON.stringify(requestData));
      console.log(`CROP_CHART 요청 전송: ${cropId}`);
    }

    setSelectedCropId(cropId);
  };

  // 선택된 작물의 최신 가격 가져오기
  const getLatestPrice = () => {
    const values = Object.values(chart);
    return values.length > 0 ? values[values.length - 1] : '데이터 없음'; // chart 배열에서 최신 가격을 반환
  };

  // 페이지 변경 함수
  const goToNextPage = () => {
    if (currentPage < totalPages) {
      setCurrentPage(currentPage + 1);
    }
  };

  const goToPrevPage = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  };

  return (
    <div className="fixed top-[140px] right-4 bg-white p-6 rounded-lg shadow-lg w-full max-w-sm z-50">
      <h2 className="text-xl font-bold text-center mb-4">구매한 작물 목록</h2>
      {currentCrops.length > 0 ? (
        <div className="space-y-2 grid grid-cols-2 gap-4 p-2">
          {currentCrops.map(([cropName, quantity]) => (
            <div
              key={cropName}
              className="flex justify-between items-center p-2 bg-gray-100 rounded-lg cursor-pointer hover:bg-gray-200"
              style={{ minHeight: '60px', display: 'flex', justifyContent: 'center', alignItems: 'center' }}  // 높이와 수직 정렬 추가
              onClick={() => handleCropClick(cropName)}  // 클릭 시 CROP_CHART 요청
            >
              <span className="flex-1 text-center whitespace-nowrap">{getCropNameInKorean(cropName)}</span>
              <span className="flex-1 text-center">{quantity}개</span>
            </div>
          ))}
        </div>
      ) : (
        <p className="text-center text-gray-500">구매한 작물이 없습니다.</p>
      )}

      {/* 페이지네이션 버튼 */}
      <div className="flex justify-between mt-4">
        <button
          onClick={goToPrevPage}
          disabled={currentPage === 1}
          className="px-4 py-2 bg-gray-200 text-gray-800 rounded-lg disabled:bg-gray-400"
        >
          이전
        </button>
        <button
          onClick={goToNextPage}
          disabled={currentPage === totalPages}
          className="px-4 py-2 bg-gray-200 text-gray-800 rounded-lg disabled:bg-gray-400"
        >
          다음
        </button>
      </div>

      {selectedCropId && (
        <div className="mt-4 p-4 bg-blue-100 rounded-lg">
          <h3 className="text-lg font-semibold">{getCropNameInKorean(selectedCropId)} 가격 정보</h3>
          <p>최신 가격: 개당 {getLatestPrice().toLocaleString()} 원</p>
        </div>
      )}

      <button
        className="mt-4 p-2 bg-red-500 text-white rounded-lg w-full hover:bg-red-600"
        onClick={onClose}
      >
        닫기
      </button>
    </div>
  );
};

export default PlayerCropModal;
