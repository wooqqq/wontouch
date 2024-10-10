import React from 'react';
import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';

interface CropChangeModalProps {
  onClose: () => void;
}

const CropChangeModal: React.FC<CropChangeModalProps> = ({ onClose }) => {
  const cropResults = useSelector((state: RootState) => state.cropResult);

  const getChangeRate = (cropName: string) => {
    const originalPrice = cropResults.originPriceMap[cropName];
    const newPrice = cropResults.newPriceMap[cropName];
    if (originalPrice && newPrice) {
      return ((newPrice - originalPrice) / originalPrice * 100).toFixed(2);
    }
    return 'N/A';
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-3xl">
        <h2 className="text-xl font-bold text-center mb-4">작물 변동률</h2>
        <div className="space-y-4 overflow-y-auto max-h-96 grid grid-cols-2 p-4">
          {Object.keys(cropResults.newPriceMap).length > 0 ? (
            Object.keys(cropResults.newPriceMap).map(cropName => (
              <div key={cropName} className="p-4 border rounded-lg shadow-md bg-white">
                <h3 className="font-semibold">{cropName}</h3>
                <p className={Number(getChangeRate(cropName)) >= 0 ? 'text-red-600' : 'text-blue-600'}> {cropResults.originPriceMap[cropName].toLocaleString()}원 {'->'} {cropResults.newPriceMap[cropName].toLocaleString()}원</p>
                <p>변동률: {getChangeRate(cropName)}% {cropResults.newPriceMap[cropName] - cropResults.originPriceMap[cropName] > 0 ? '상승' : cropResults.newPriceMap[cropName] - cropResults.originPriceMap[cropName] === 0 ? '동결' : '하락'}</p>
              </div>
            ))
          ) : (
            <p className="text-gray-500 text-center">작물 변동률 정보가 없습니다.</p>
          )}
        </div>
        <button
          className="mt-4 p-2 bg-red-500 text-white rounded-lg w-full hover:bg-red-600"
          onClick={onClose}
        >
          닫기
        </button>
      </div>
    </div>
  );
};

export default CropChangeModal;
