import React from 'react';
import axios from 'axios';
import { useSelector } from 'react-redux';
import { RootState } from '../../../redux/store';

import cancel from '../../../assets/icon/cancel.png';
import confirm from '../../../assets/icon/confirm.png';

export default function FriendDelete({
  closeDeleteModal,
  friendId,
}: {
  closeDeleteModal: () => void;
  friendId: number;
}) {
  const API_LINK = import.meta.env.VITE_API_URL;
  const userId = useSelector((state: RootState) => state.user.id);

  // 삭제 요청
  const deleteFriend = async () => {
    try {
      const response = await axios.delete(`${API_LINK}/friend/delete`, {
        data: {
          userId: userId,
          friendId: friendId,
        },
      });
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className="yellow-box w-2/5 h-[300px] p-6 px-10 border-[#36EAB5] bg-[#FFFEEE]">
      <div className="mint-title text-5xl">친구 끊기</div>
      <div>ㅇㅇㅇ 님과 친구를 끊겠습니까?</div>
      <div>
        <div>
          <img src={cancel} alt="취소" onClick={closeDeleteModal} />
        </div>
        <div>
          <img src={confirm} alt="친구 삭제" onClick={deleteFriend} />
        </div>
      </div>
    </div>
  );
}
