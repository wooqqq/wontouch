import React from 'react';

import cancel from '../../../assets/icon/cancel.png';

const API_LINK = import.meta.env.VITE_API_URL;

export default function FindRoom({
  closeFindRoom,
}: {
  closeFindRoom: () => void;
}) {
  return (
    <div className="yellow-box w-1/2 h-[300px] border-[#36EAB5] bg-[#FFFEEE] p-8">
      <div className="relative">
        <div className="mint-title">방 찾기</div>
        <div
          className="absolute right-0 top-1/2 transform -translate-y-1/2"
          onClick={closeFindRoom}
        >
          <img src={cancel} alt="메일함 닫기" />
        </div>
      </div>
      <div className="flex justify-center mt-4">
        <input
          type="text"
          placeholder="방 제목"
          className="input-tag font-['Galmuri11'] w-10/12 h-[80px] p-4 text-2xl"
        />
      </div>
    </div>
  );
}
