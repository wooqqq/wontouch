import React from 'react';
import { useState } from 'react';
import Modal from './Modal';
import FriendProfile from '../lobby/friend/FriendProfile';

interface FriendInfoProps {
  friendId: number;
  nickname: string;
  description: string;
  characterName: string;
  tierPoint: number;
}

export default function FriendInfo({
  friendId,
  nickname,
  description,
  characterName,
  tierPoint,
}: FriendInfoProps) {
  const [showProfile, setShowProfile] = useState<boolean>(false);

  // 사용자 프로필 모달
  const openProfile = () => {
    setShowProfile(true);
  };

  const closeProfile = () => {
    setShowProfile(false);
  };

  return (
    <div className="relative">
      <button
        className="ranking-box white-text text-2xl h-[40px] w-full mb-1"
        onClick={openProfile}
      >
        {nickname}
        <div className="absolute right-4">
          {/* <img src={character} alt="메달" className="w-6 h-7" /> */}
        </div>
      </button>

      {showProfile && (
        <Modal>
          <FriendProfile closeProfile={closeProfile} friendId={friendId} />
        </Modal>
      )}
    </div>
  );
}
