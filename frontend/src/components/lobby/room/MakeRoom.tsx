import React, { useState } from 'react';
import axios from 'axios';
import { RootState } from '../../../redux/store';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import cancel from '../../../assets/icon/cancel.png';
import confirm from '../../../assets/icon/confirm.png';
import lock from '../../../assets/icon/lock.png';
import unLock from '../../../assets/icon/unlock.png';

// api 주소
const API_LINK = import.meta.env.VITE_API_URL;

export default function MakeRoomModal({
  closeMakeRoom,
}: {
  closeMakeRoom: () => void;
}) {
  const navigate = useNavigate();

  // UUID 먼저 발급
  const getUUID = async () => {
    try {
      const res = await axios.get(`${API_LINK}/room/create/random-uuid`);
      const UUID = res.data.data;
      return UUID; // UUID 값을 반환
    } catch (error) {
      console.error('UUID 발급 중 에러 발생', error);
    }
  };

  // store에 저장된 userId
  const userId = useSelector((state: RootState) => state.user.id);

  const [roomName, setRoomName] = useState<string>('');
  const [isPrivate, setIsPrivate] = useState<boolean>(false);
  const [password, setPassword] = useState<string>('');

  // 비밀번호 입력 시 isPrivate 값을 설정
  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setPassword(value);
    setIsPrivate(value !== ''); // 비밀번호가 있으면 isPrivate을 true로, 없으면 false로 설정
  };

  const handleMakeRoom = async () => {
    try {
      // UUID 발급
      const UUID = await getUUID();
      if (!UUID) {
        alert('UUID 발급에 실패했습니다.');
        return;
      }

      // 방 생성 요청
      const res = await axios.post(`${API_LINK}/room`, {
        roomId: UUID,
        roomName,
        hostPlayerId: userId,
        isPrivate,
        password: isPrivate ? password : '', // 비공개 방일 경우에만 비밀번호를 설정
      });

      const createdRoomId = res.data.data.roomId;
      navigate(`/wait/${createdRoomId}`);
    } catch (error) {
      console.error('방 생성 중 에러 발생', error);
    }
  };

  return (
    <div className="yellow-box w-1/2 h-[460px] border-[#36EAB5] bg-[#FFFEEE] p-8 px-20">
      <div className="mint-title mb-6">방 만들기</div>
      <div>
        <input
          type="text"
          placeholder="방 제목"
          className="input-tag font-['Galmuri11'] w-full h-[80px] p-4 text-2xl mb-12"
          value={roomName}
          onChange={(e) => setRoomName(e.target.value)}
        />
      </div>
      <div className="flex items-center justify-between">
        <span>
          <img
            src={isPrivate ? lock : unLock}
            alt={isPrivate ? '비밀번호 있는 방' : '비밀번호 없는 방'}
          />
        </span>
        <input
          type="text"
          placeholder="비밀번호"
          className="input-tag font-['Galmuri11'] w-10/12 h-[80px] p-4 text-2xl"
          value={password}
          onChange={handlePasswordChange} // 비밀번호 입력 시 isPrivate 업데이트
        />
      </div>
      <div className="mt-12 flex justify-between px-48">
        <button onClick={closeMakeRoom}>
          <img src={cancel} alt="모달 닫기" />
        </button>
        <button onClick={handleMakeRoom}>
          <img src={confirm} alt="방 생성하기" />
        </button>
      </div>
    </div>
  );
}
