import React from 'react';
import FriendInfo from '../common/FriendInfo';

export default function Friend() {
  return (
    <div className="yellow-box w-11/12 h-[218px] ml-6">
      <div className="mint-title">랭킹 TOP 8</div>
      <div className="mx-8">
        <FriendInfo />
      </div>
    </div>
  );
}
