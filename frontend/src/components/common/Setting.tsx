import React from 'react';

import setting from '../../assets/icon/setting.png';
import cancel from '../../assets/icon/cancel.png';

export default function Setting({
  closeSetting,
}: {
  closeSetting: () => void;
}) {
  return (
    <div className="setting-box w-1/3 h-[600px] p-5">
      <div className="relative">
        <div className="setting-text text-4xl">환경 설정</div>
        <div
          className="absolute right-6 top-1/2 transform -translate-y-1/2"
          onClick={closeSetting}
        >
          <img src={cancel} alt="환경설정 닫기" />
        </div>
      </div>
      <div className="">
        <div className="audio-on text-3xl">오디오 ON</div>
        <div className="setting-list text-3xl">캐릭터 수정</div>
        <div className="setting-list text-3xl">회원 정보 수정</div>
        <div className="setting-list text-3xl">로그아웃</div>
      </div>
      <div className="version text-2xl">version 0.0.1</div>
    </div>
  );
}
