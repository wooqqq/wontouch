import React, { useEffect } from 'react';

interface ModalProps {
  houseNum: number | null;
  closeModal: () => void;
}

const InteractionModal: React.FC<ModalProps> = (
  { houseNum, closeModal },
) => {

  const handleKeyDown = (event: KeyboardEvent) => {
    if(event.key === 'Escape'){
      closeModal();
    }
  };

  useEffect(() => {
    //이벤트 등록
    window.addEventListener('keydown', handleKeyDown);

    //모달 닫히면 이벤트 제거
    return () => {
      window.removeEventListener('keydown', handleKeyDown);
    }
  }, [])//랜더링시 한번만 되야하므로!

  if (houseNum === null) return null;
  else console.log('뜸!'); // houseNum이 null이면 모달을 표시하지 않음

  return (
    <>
      {/* 모달 뒤에 어두운 배경 */}
      <div
        className="fixed inset-0 bg-black bg-opacity-50 z-1000"
        onClick={closeModal}
      ></div>

      {/* 모달 창 */}
      <div className="fixed inset-0 flex items-center justify-center z-50">
        <div className="bg-white p-6 rounded-lg shadow-lg w-80">
          <div className="modal-content">
            {houseNum === 0 ? (
              <h2 className="text-2xl font-bold mb-4">거래소</h2>
            ) : (
              <h2 className="text-2xl font-bold mb-4">{houseNum}번 집</h2>
            )}
            <button
              className="mt-4 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition"
              onClick={closeModal}
            >
              닫기
            </button>
          </div>
        </div>
      </div>
    </>
  );
};

export default InteractionModal;
