import React from 'react';
import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';
import board from '../../assets/game/board.png';
import cancel from '../../assets/icon/cancel.png';

const GameResultModal: React.FC<{ onClose: () => void }> = ({ onClose }) => {
  const results = useSelector((state: RootState) => state.gameResult.results);


  // rank에 따라 색상을 반환하는 함수
  const getRankColor = (rank: number) => {
    switch (rank) {
      case 1:
        return "gold";  // 금색
      case 2:
        return "silver";  // 은색
      case 3:
        return "#cd7f32";  // 동색 (청동색)
      default:
        return "black";  // 기본 색상
    }
  };

  return (
    <>
      <div className="fixed inset-0 bg-black bg-opacity-50 z-50" onClick={onClose}></div>

      <div className="fixed inset-0 flex items-center justify-center z-50">
        <div className="relative p-8 rounded-lg shadow-lg w-[70%] h-auto bg-white z-30">
          <img src={board} alt="배경 이미지" className="absolute top-0 left-0 w-full h-full z-10 opacity-20" />
          <button onClick={onClose} className="absolute top-5 right-5 z-40">
            <img src={cancel} alt="닫기" className="w-8 h-8" />
          </button>

          <div className="relative z-20">
            <h2 className="text-4xl font-bold text-center mb-8">게임 결과</h2>
            <div className="grid grid-cols-4 gap-6 text-center font-semibold text-lg">
              <div>Rank</div>
              <div>Player ID</div>
              <div>Total Gold</div>
              <div>Mileage</div>
              {results.map((result) => (
                <React.Fragment key={result.playerId}>
                  <div className="py-3" style={{ color: getRankColor(result.rank) }}>{result.rank}</div>
                  <div className="py-3">{result.playerId}</div>
                  <div className="py-3 text-yellow-500">{result.totalGold.toLocaleString()} G</div>
                  <div className="py-3">{result.mileage} pts</div>
                </React.Fragment>
              ))}
            </div>
            <div className="mt-8 flex justify-center">
              <button
                className="px-6 py-3 bg-blue-500 text-white font-semibold rounded-lg hover:bg-blue-600"
                onClick={onClose}
              >
                나가기
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default GameResultModal;
