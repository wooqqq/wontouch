import FriendInfo from '../common/FriendInfo';
import CancelIcon from '../../assets/icon/cancel.png';
import { useState } from 'react';

// 예시 친구 데이터 (필요에 따라 props로 받거나 API로부터 가져올 수 있음)
const friendsList = [
  { id: 1, name: '김부르주아' },
  { id: 2, name: '콘샐러드냠냠' },
  { id: 3, name: 'HelloWorld' },
  { id: 4, name: '인생한방' },
  { id: 5, name: '23토끼' },
  { id: 6, name: '감자도리' },
  // 친구 수에 따라 추가
];

function FriendInvite({ onClose }: { onClose: () => void }) {
  const [invitedFriends, setInvitedFriends] = useState(
    Array(friendsList.length).fill(false), // 초기값은 모두 초대되지 않은 상태
  );

  const toggleInvite = (index: number) => {
    // 해당 친구의 초대 상태를 토글
    setInvitedFriends((prev) => {
      const newInvites = [...prev];
      newInvites[index] = !newInvites[index];
      return newInvites;
    });
  };

  return (
    <div className="yellow-box p-[37px] h-[400px] border-[#36eab5] bg-[#FFFEEE] relative">
      <div className="mint-title text-center">친구 초대</div>
      <button
        onClick={onClose}
        className="absolute top-[10px] right-[10px] bg-none"
      >
        <img src={CancelIcon} alt="닫기 버튼" />
      </button>
      <section className="bg-[#E6E2C2] h-4/5 pl-[14px] pr-[30px] py-5 rounded-[10px] overflow-x-hidden overflow-y-scroll">
        <div>
          {friendsList.map((friend, index) => (
            <div className="flex mb-4" key={friend.id}>
              {/* <FriendInfo */}
              {/* // friend={friend}
              /> */}
              {/* FriendInfo에 friend 데이터를 전달 */}
              <button
                onClick={() => !invitedFriends[index] && toggleInvite(index)}
                className={`ml-2 rounded-xl px-4 min-w-[100px] text-white ${invitedFriends ? 'bg-gray-400 cursor-default' : 'bg-[#896A65]'}`}
                style={{
                  pointerEvents: invitedFriends[index] ? 'none' : 'auto',
                }}
              >
                {invitedFriends[index] ? '초대완료' : '초대'}
              </button>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}

export default FriendInvite;
