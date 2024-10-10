import React from 'react';
import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';
import coin from '../../assets/icon/coin_gold_mini.png';

const BalanceDisplay: React.FC = () => {
  const balance = useSelector((state: RootState) => state.balance.balance);

  return (
    <div className="fixed top-0.5 right-0 shadow-lg py-4 px-10 rounded-lg z-50 flex justify-between items-center">
      <img src={coin} className='mr-3' />
      <p className="yellow-text font-semibold text-[20px]">{balance.toLocaleString()}</p>
    </div>
  );
};

export default BalanceDisplay;
