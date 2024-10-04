import { useEffect, useState } from 'react';
import axios from 'axios';

import { RoomUserInfo } from './RoomUserInfo';
import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';

interface RoomUserListProps {
  onOpen: () => void;
  socket: WebSocket | null;
}

function RoomUserList({ onOpen }: RoomUserListProps) {
  const API_LINK = import.meta.env.VITE_API_URL;
  const token = localStorage.getItem('access_token');
  const participants = useSelector(
    (state: RootState) => state.room.participants,
  );
  const hostId = useSelector((state: RootState) => state.room.HostId);
  // 고정된 유저 수 (8명)
  const totalUsers = 8;
  const [users, setUsers] = useState<any[]>([]);

  // 참가자 정보 가져오기 API
  const fetchUsersInfo = async () => {
    try {
      // 기존 유저 데이터 초기화
      setUsers([]);

      // participants가 존재하는지 체크
      if (!participants || participants.length === 0) return;
      console.log(participants);
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
      console.log(fetchUsers);
    } catch (error) {
      console.error('유저 정보를 가져오는 중 오류 발생: ', error);
    }
  };

  useEffect(() => {
    fetchUsersInfo();
  }, [participants]);

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
              isHost={user.userId === hostId}
              playerId={user.userId}
              nickname={user.nickname}
              character={user.characterName}
              isReady={user.isReady}
              tierPoint={user.tierPoint}
            />
          ) : (
            <button onClick={onOpen}>
              <RoomUserInfo
                isHost={false}
                playerId={undefined}
                nickname={undefined}
                character={undefined}
                isReady={false}
                tierPoint="0"
              />
            </button>
          )}
        </div>
      ))}
    </div>
  );
}
export default RoomUserList;
