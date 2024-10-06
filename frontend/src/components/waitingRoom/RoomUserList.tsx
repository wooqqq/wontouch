import { RoomUserInfo } from './RoomUserInfo';
import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';

interface RoomUserListProps {
  onOpen: () => void;
  socket: WebSocket | null;
  users: any[];
}

function RoomUserList({ onOpen, socket, users }: RoomUserListProps) {
  const hostId = useSelector((state: RootState) => state.room.hostId);
  // 고정된 유저 수 (8명)
  const totalUsers = 8;

  // 고정된 길이의 배열 생성
  const userList = Array.from({ length: totalUsers }, (_, index) => {
    // 실제 유저 데이터로 대체
    const user = users[index] || {
      isHost: false,
      playerId: undefined,
      nickname: '',
      characterName: '',
      isReady: false,
      tierPoint: 0,
      mileage: 0,
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
