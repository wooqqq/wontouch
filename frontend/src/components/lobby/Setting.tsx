import React from 'react';
import { useNavigate } from 'react-router-dom';

import cancel from '../../assets/icon/cancel.png';

export default function Setting({
  closeSetting,
}: {
  closeSetting: () => void;
}) {
  const navigate = useNavigate();
  const changeInfo = () => {
    navigate('/setting');
  };
  return (
    <div className="setting-box w-1/3 h-[600px] p-5">
      <div className="relative">
        <div className="setting-text text-4xl">환경 설정</div>
        <button
          className="absolute right-6 top-1/2 transform -translate-y-1/2"
          onClick={closeSetting}
        >
          <img src={cancel} alt="환경설정 닫기" />
        </button>
      </div>
      <div className="">
        <button className="audio-on text-3xl w-4/5 my-6 mt-10">
          오디오 ON
        </button>
        <button
          className="setting-list text-3xl w-4/5 my-6"
          onClick={changeInfo}
        >
          캐릭터 수정
        </button>
        <button
          className="setting-list text-3xl w-4/5 my-6"
          onClick={changeInfo}
        >
          회원 정보 수정
        </button>
        <button className="setting-list text-3xl w-4/5 my-6">로그아웃</button>
      </div>
      <div className="version text-2xl mt-6">version 0.0.1</div>
    </div>
  );
}
