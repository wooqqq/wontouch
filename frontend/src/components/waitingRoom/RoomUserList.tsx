import { useEffect, useId, useState } from 'react';
import { RoomUserInfo } from './RoomUserInfo';
import axios from 'axios';

interface RoomUserListProps {
  participants: string[];
  roomHost: number;
  onOpen: () => void;
}

function RoomUserList({ participants, roomHost, onOpen }: RoomUserListProps) {
  const API_LINK = import.meta.env.VITE_API_URL;
  const token = localStorage.getItem('access_token');

  // 고정된 유저 수 (8명)
  const totalUsers = 8;
  const [users, setUsers] = useState<any[]>([]);

  // 참가자 정보 가져오기 API
  useEffect(() => {
    const fetchUsersInfo = async () => {
      try {
        // participants가 존재하는지 체크
        if (!participants || participants.length === 0) return;

        const fetchUsers = await Promise.all(
          participants.map(async (userId) => {
            const userResponse = await axios.get(`${API_LINK}/user/${userId}`, {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            });
            return userResponse.data.data;
          }),
        );
        setUsers(fetchUsers);
        console.log(users);
      } catch (error) {
        console.error('유저 정보를 가져오는 중 오류 발생: ', error);
      }
    };
    fetchUsersInfo();
  }, [participants, API_LINK]);

  // 고정된 길이의 배열 생성
  const userList = Array.from({ length: totalUsers }, (_, index) => {
    // 실제 유저 데이터로 대체
    const user = users[index] || {
      isHost: false,
      playerId: undefined,
      isReady: false,
    };
    return user;
  });

  return (
    <div className="waitingroom-brown-box flex flex-wrap justify-center gap-9">
      {userList.map((user, index) => (
        <div key={index} className="flex-shrink-0">
          {/* playerId가 없을 경우에만 클릭 이벤트를 허용 */}
          {user.userId ? (
            <RoomUserInfo
              isHost={user.userId === roomHost}
              playerId={user.userId}
              nickname={user.nickname}
              character={user.characterName}
              isReady={false}
            />
          ) : (
            <button onClick={onOpen}>
              <RoomUserInfo
                isHost={false}
                playerId={undefined}
                nickname={undefined}
                character={undefined}
                isReady={false}
              />
            </button>
          )}
        </div>
      ))}
    </div>
  );
}
export default RoomUserList;
