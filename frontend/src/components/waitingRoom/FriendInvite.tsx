import FriendInfo from '../common/FriendInfo';
import CancelIcon from '../../assets/icon/cancel.png';
import { useEffect, useState } from 'react';
import axios from 'axios';
import { setFriends, setOnlineFriends } from '../../redux/slices/friendSlice';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../redux/store';
import LevelImg from '../common/LevelImg';

// 1. 게임방 내 친구 초대에 사용되는 url
//  - 친구 관리 > 온라인인 친구 목록 조회(`GET:http://localhost:8081/api/friend/online/{userId}`) : 온라인인 친구 불러오기
// response:
// {
//   "status": 200,
//   "message": "온라인인 친구 목록 조회 성공",
//   "data": [
//     {
//       "friendId": 3,
//       "nickname": "나폴리맛피아",
//       "description": null,
//       "characterName": "boy",
//       "tierPoint": 0,
//       "online": true
//     }
//   ]
// }

//  - 게임 초대 > 게임 초대 보내기(`POST:http://localhost:8081/api/room/invite`) : 친구 목록에서 버튼으로 이 url 연결하면 됨

// 2. 게임 초대 알림 상세 조회
//  - 알림 > 게임 초대 상세 조회(`GET:http://localhost:8081/api/notification/detail/{id}`) : 타입이 `GAME_INVITE`인 알림 한정으로 사용할 수 있는 상세 조회

// 3. 게임 초대 수락 / 거절
//  - 게임 초대 알림 상세 조회에서 찾을 수 있는 정보를 통해 게임방 들어가는 api 불러오면 될듯

function FriendInvite({ onClose }: { onClose: () => void }) {
  const dispatch = useDispatch();
  const userId = useSelector((state: RootState) => state.user.id);
  const roomId = useSelector((state: RootState) => state.room.roomId);
  const onlineFriendsList = useSelector(
    (state: RootState) => state.friend.onlineFriends,
  );
  const [invitedFriends, setInvitedFriends] = useState<boolean[]>([]);

  useEffect(() => {
    // 온라인 친구 목록을 API로부터 가져오는 함수
    const fetchOnlineFriends = async (userId: number | null) => {
      try {
        const response = await axios.get(
          `http://localhost:8081/api/friend/online/${userId}`,
        );

        if (response.data.status === 200) {
          dispatch(setOnlineFriends(response.data.data));
          setInvitedFriends(Array(response.data.data.length).fill(false)); // 초대 상태 초기화
          console.log(response.data.data);
        }
      } catch (error) {
        console.error('친구 목록 가져오기 실패: ', error);
      }
    };

    fetchOnlineFriends(userId);
  }, [userId, roomId]);

  const toggleInvite = (index: number) => {
    // 해당 친구의 초대 상태를 토글
    setInvitedFriends((prev) => {
      const newInvites = [...prev];
      newInvites[index] = !newInvites[index];

      // 초대 요청 API 호출
      if (newInvites[index]) {
        inviteFriend(onlineFriendsList[index].friendId);
      }

      return newInvites;
    });
  };

  const inviteFriend = async (friendId: number) => {
    try {
      const response = await axios.post(
        'http://localhost:8081/api/room/invite',
        { senderId: userId, receiverId: friendId, roomId: roomId },
      );
      if (response.data.status === 200) {
        console.log(`친구 ${friendId}에게 초대 완료`);
      } else {
        console.error('초대 실패:', response.data.message);
      }
    } catch (error) {
      console.error('초대 요청 실패:', error);
    }
  };

  return (
    <div className="yellow-box p-[37px] h-[400px] border-[#36eab5] bg-[#FFFEEE] relative">
      <div className="mint-title text-center">게임 초대 가능한 친구</div>
      <button
        onClick={onClose}
        className="absolute top-[10px] right-[10px] bg-none"
      >
        <img src={CancelIcon} alt="닫기 버튼" />
      </button>
      <section className="bg-[#E6E2C2] h-4/5 pl-[14px] pr-[30px] py-5 rounded-[10px] overflow-x-hidden overflow-y-scroll">
        <div>
          {onlineFriendsList && onlineFriendsList.length > 0 ? (
            onlineFriendsList.map((friend, index) => (
              <div className="flex mb-4" key={friend.friendId}>
                <div>{friend.nickname}</div>
                <LevelImg tierPoint={friend.tierPoint} />
                <button
                  onClick={() => !invitedFriends[index] && toggleInvite(index)}
                  className={`ml-2 rounded-xl px-4 min-w-[100px] text-white ${invitedFriends[index] ? 'bg-gray-400 cursor-default' : 'bg-[#896A65]'}`}
                  style={{
                    pointerEvents: invitedFriends[index] ? 'none' : 'auto',
                  }}
                >
                  {invitedFriends[index] ? '초대완료' : '초대'}
                </button>
              </div>
            ))
          ) : (
            <p>온라인 친구가 없습니다.</p> // 친구가 없을 때 표시할 메시지
          )}
        </div>
      </section>
    </div>
  );
}

export default FriendInvite;
