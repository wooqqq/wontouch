import React from 'react';
import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';
import LevelImg from './LevelImg';

export default function Nickname() {
  const userNickname = useSelector((state: RootState) => state.user.nickname);
  const userTierPoint = useSelector((state: RootState) => state.user.tierPoint);

  return (
    <div className="flex items-center">
      <span className="white-text text-2xl">{userNickname}</span>
      <div className="w-7 ml-6">
        <LevelImg tierPoint={userTierPoint ? userTierPoint : 0} />
      </div>
    </div>
  );
}
