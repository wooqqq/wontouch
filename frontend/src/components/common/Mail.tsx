import React from 'react';

import mail from '../../assets/icon/mail.png';
import chat from '../../assets/icon/expression_chat.png';
import cancel from '../../assets/icon/cancel.png';
import confirm from '../../assets/icon/confirm.png';

export default function Mail({ closeMail }: { closeMail: () => void }) {
  return (
    <div className="yellow-box w-1/2 h-[470px] p-6 px-10 pb-10 border-[#36EAB5]">
      <div className="relative mb-4">
        <div className="flex justify-center mint-title text-5xl">메일함</div>
        <button
          className="absolute right-0 top-1/2 transform -translate-y-1/2"
          onClick={closeMail}
        >
          <img src={cancel} alt="메일함 닫기" />
        </button>
      </div>

      <div className="list-box h-5/6 p-4">
        <div className="flex p-4">
          <div>
            <img src={mail} alt="" />
          </div>
          <button className="flex items-center white-text text-xl mx-5">
            태태태쏭 님에게 친구 요청이 도착했습니다.
          </button>
        </div>
        {/* 삭제할 부분 */}
        <div className="flex p-4 relative">
          <div>
            <img src={mail} alt="" />
          </div>
          <div className="flex items-center white-text text-xl mx-5">
            태태태쏭 님에게 게임 초대가 도착했습니다.
          </div>
          <div className="absolute right-3">
            <img src={confirm} alt="" />
          </div>
        </div>
        <div className="flex p-4 relative">
          <div>
            <img src={mail} alt="" />
          </div>
          <div className="flex items-center white-text text-xl mx-5">
            [공지사항] 내일 새로운 맵이 나옵니다!!(글자 제한?)
          </div>
          <div className="absolute right-3">
            <img src={confirm} alt="" />
          </div>
        </div>
      </div>
    </div>
  );
}
