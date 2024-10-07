import axios from 'axios';
import { useSelector } from 'react-redux';
import { RootState } from '../../../redux/store';

import cancel from '../../../assets/icon/cancel.png';
import confirm from '../../../assets/icon/confirm.png';

export default function FriendDelete({
  closeDeleteModal,
  friendId,
  nickname,
}: {
  closeDeleteModal: () => void;
  friendId: number;
  nickname: string;
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
      // 페이지 새로고침
      window.location.reload();
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className="yellow-box w-2/5 h-[250px] p-6 px-10 border-[#36EAB5] bg-[#FFFEEE]">
      <div className="mint-title text-5xl mb-8">친구 끊기</div>
      <div className="white-text text-3xl mb-8">
        {nickname} 님과 친구를 끊겠습니까?
      </div>
      <div className="flex justify-between px-36">
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
