import { RoomUserInfo } from './RoomUserInfo';
import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';
import selectboxBL from '../../assets/icon/selectbox_bl.png';
import selectboxBR from '../../assets/icon/selectbox_br.png';
import selectboxTL from '../../assets/icon/selectbox_tl.png';
import selectboxTR from '../../assets/icon/selectbox_tr.png';

interface RoomUserListProps {
  onOpen: () => void;
  socket: WebSocket | null;
}

function RoomUserList({ onOpen, socket }: RoomUserListProps) {
  const hostId = useSelector((state: RootState) => state.room.hostId);
  const gameParticipants = useSelector(
    (state: RootState) => state.room.gameParticipants,
  );
  // 고정된 유저 수 (8명)
  const totalUsers = 8;

  // 고정된 길이의 배열 생성
  const userList = Array.from({ length: totalUsers }, (_, index) => {
    // 실제 유저 데이터로 대체
    const user = gameParticipants[index] || {
      isHost: false,
      playerId: 0,
      nickname: '',
      characterName: '',
      isReady: false,
      tierPoint: 0,
      mileage: 0,
    };
    return user;
  });

  return (
    <div className="waitingroom-brown-box flex flex-wrap justify-center gap-9 mt-8 mb-6">
      <img
        src={selectboxBL}
        alt="박스 왼쪽 하단"
        className="absolute left-[-10px] top-[460px]"
      />
      <img
        src={selectboxTR}
        alt="박스 오른쪽 상단"
        className="absolute right-[400px] top-[18px]"
      />
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
                playerId={0}
                nickname={undefined}
                character={''}
                isReady={false}
                tierPoint={0}
              />
            </button>
          )}
        </div>
      ))}
    </div>
  );
}
export default RoomUserList;
