import React from 'react';

interface ResultModalProps {
  round: number;
  onNextRound: () => void;
}

const ResultModal: React.FC<ResultModalProps> = ({ round, onNextRound }) => {
  return (
    <div className="fixed inset-0 flex items-center justify-center z-50">
      <div className="bg-white p-6 rounded shadow-lg">
        <h2 className="text-xl font-bold">Round {round} Completed!</h2>
        <button
          className="bg-blue-500 text-white px-4 py-2 mt-4"
          onClick={onNextRound}
        >
          Next Round
        </button>
      </div>
    </div>
  );
};

export default ResultModal;
