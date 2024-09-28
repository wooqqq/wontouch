import React from 'react';
import axios from 'axios';
import { RootState } from '../../redux/store';
import { useState } from 'react';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import cancel from '../../assets/icon/cancel.png';
import confirm from '../../assets/icon/confirm.png';
import lock from '../../assets/icon/lock.png';
import unLock from '../../assets/icon/unlock.png';

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

  const handleMakeRoom = async () => {
    try {
      // UUID 발급
      const UUID = await getUUID(); // getUUID를 호출하고 UUID를 기다림
      if (!UUID) {
        alert('UUID 발급에 실패했습니다.');
        return;
      }

      // 발급된 UUID로 방 생성 요청
      const res = await axios.post(`${API_LINK}/room`, {
        roomId: UUID,
        roomName: roomName,
        hostPlayerId: userId,
        isPrivate: isPrivate,
        password: isPrivate ? password : '',
      });

      // 방 생성 후 생성한 방으로 바로 이동
      navigate(`/waiting-room/${UUID}`);
    } catch (error) {
      console.error('방 생성 중 에러 발생', error);
    }
  };

  const clickPrivate = () => {
    setIsPrivate(!isPrivate);
    // 비밀번호가 없는 방이라면 비밀번호 초기화
    if (!isPrivate) {
      setPassword('');
    }
  };

  return (
    <div className="yellow-box w-[725px] h-[500px] border-[#36EAB5] bg-[#FFFEEE]">
      <div className="mint-title">방 만들기</div>
      {/* 방 제목 입력 */}
      <div>
        <input
          type="text"
          placeholder="방 제목"
          className="input-tag"
          value={roomName}
          onChange={(e) => setRoomName(e.target.value)}
        />
      </div>
      {/* 비밀번호 설정 */}
      <div>
        <span onClick={clickPrivate}>
          <img
            src={isPrivate ? lock : unLock}
            alt={isPrivate ? '비밀번호 있는 방' : '비밀번호 없는 방'}
          />
        </span>
        {/* 비밀번호 입력 필드 */}
        <input
          type="text"
          placeholder="비밀번호"
          className="input-tag"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          disabled={!isPrivate}
        />
      </div>
      <div>
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
