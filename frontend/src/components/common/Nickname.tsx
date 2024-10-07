import React from 'react';
import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';
import LevelImg from './LevelImg';

export default function Nickname() {
  const userNickname = useSelector((state: RootState) => state.user.nickname);
  const userTierPoint = useSelector((state: RootState) => state.user.tierPoint);

  return (
    <div className="flex items-center brown-box w-56 h-12 justify-between px-4">
      <span className="white-text text-2xl">{userNickname}</span>
      <div className="w-8">
        <LevelImg tierPoint={userTierPoint ? userTierPoint : 0} />
      </div>
    </div>
  );
}
