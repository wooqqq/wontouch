import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';
import CropChangeModal from './CropChangeModal'; // CropChangeModal 추가
import { ResultModalProps } from './types';

const ResultModal: React.FC<ResultModalProps> = ({ round, onNextRound }) => {
  const articleResults = useSelector((state: RootState) => state.articleResult.articleResults);
  const preparationTime = useSelector((state: RootState) => state.time.preparationTime);

  const [expandedArticleIndex, setExpandedArticleIndex] = useState<number | null>(null);
  const [isButtonDisabled, setIsButtonDisabled] = useState(false);
  const [isCropModalOpen, setIsCropModalOpen] = useState(false); // 작물 모달 상태

  const toggleArticleExpansion = (index: number) => {
    setExpandedArticleIndex(expandedArticleIndex === index ? null : index);
  };

  const handleClick = () => {
    setIsButtonDisabled(true);
    onNextRound();
  };

  const openCropModal = () => {
    setIsCropModalOpen(true);
  };

  const closeCropModal = () => {
    setIsCropModalOpen(false);
  };

  return (
    <div className="fixed inset-0 flex items-center justify-center z-50">
      <div className="bg-white p-6 lg:p-8 rounded-lg shadow-lg w-full max-w-4xl">
        <h2 className="text-2xl font-bold text-center">Round {round} 종료!</h2>
        <p className="text-center mb-6">{preparationTime}초 뒤, 다음 라운드로 넘어갑니다.</p>

        <div className="space-y-6 overflow-y-auto max-h-96">
          {articleResults.length > 0 ? (
            articleResults.map((article, index) => (
              <div
                key={index}
                className="p-4 border rounded-lg shadow-md bg-white hover:bg-gray-50 transition-all"
              >
                <h3
                  className="text-lg font-semibold text-blue-500 cursor-pointer hover:underline"
                  onClick={() => toggleArticleExpansion(index)}
                >
                  {article.title}
                </h3>
                <p className="text-gray-700 mt-2">
                  <span className="font-semibold">변동률: </span>
                  {article.change_rate >= 0 ? (
                    <span className="text-blue-600">{article.change_rate}%</span>
                  ) : (
                    <span className="text-red-600">{article.change_rate}%</span>
                  )}
                </p>
                {article.sub_crops.length > 0 && (
                  <div className="mt-2">
                    <h4 className="font-semibold text-gray-600">관련 작물:</h4>
                    <ul className="list-disc list-inside">
                      {article.sub_crops.map((crop, cropIndex) => (
                        <li key={cropIndex}>
                          {crop.change_rate >= 0 ? (
                            <span className="text-blue-600">{crop.name}: {crop.change_rate}%</span>
                          ) : (
                            <span className="text-red-600">{crop.name}: {crop.change_rate}%</span>
                          )}
                        </li>
                      ))}
                    </ul>
                  </div>
                )}
                {expandedArticleIndex === index && (
                  <div className="mt-4">
                    <h4 className="font-semibold text-gray-800">기사 내용:</h4>
                    <p className="text-gray-600">{article.body}</p>
                    <p className="text-gray-500 mt-2 text-sm">작성자: {article.author}</p>
                  </div>
                )}
              </div>
            ))
          ) : (
            <p className="text-gray-500 text-center">기사 결과가 없습니다.</p>
          )}
        </div>

        {/* 작물 변동률 버튼 추가 */}
        <button
          className="bg-yellow-500 text-white px-6 py-3 mt-6 w-full text-xl hover:bg-yellow-600"
          onClick={openCropModal}
        >
          작물 변동률 보기
        </button>

        <button
          className={`bg-blue-500 text-white px-6 py-3 mt-4 w-full text-xl ${isButtonDisabled ? 'opacity-50 cursor-not-allowed' : 'hover:bg-blue-600'}`}
          onClick={handleClick}
          disabled={isButtonDisabled}
        >
          준비 완료!
        </button>
      </div>

      {/* CropChangeModal 모달 표시 */}
      {isCropModalOpen && <CropChangeModal onClose={closeCropModal} />}
    </div>
  );
};

export default ResultModal;
