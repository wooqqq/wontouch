import React from 'react';

import lock from '../../assets/icon/lock.png';

export default function RoomList() {
  return (
    <div className="room-box w-96 h-36 m-3 p-2">
      <div className="room-info p-0.5 px-2 mb-2">
        <span className="text-lg font-['Galmuri11-bold'] text-yellow-300 mr-2.5">
          001
        </span>
        <span className="text-lg font-['Galmuri11'] text-white text-center">
          방 제목
        </span>
      </div>
      <div className="room-info flex p-2">
        <span className="mr-4">
          <img src="src/assets/tmp.png" alt="" className="w-72 h-16" />
        </span>
        <div className="flex flex-col items-center justify-center">
          <div>
            <img src={lock} alt="" className="w-6 h-8" />
          </div>
          <div>
            <div className="white-text">3/8</div>
          </div>
        </div>
      </div>
    </div>
  );
}
