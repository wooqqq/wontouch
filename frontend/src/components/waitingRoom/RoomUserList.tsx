import { useId } from 'react';
import { RoomUserInfo } from './RoomUserInfo';

function RoomUserList({ onOpen }: { onOpen: () => void }) {
  // 고정된 유저 수 (8명)
  const totalUsers = 8;

  // 유저 예시 (실제로는 플레이어들이 담길 것)
  // 호스트 여부 - HostId == userId
  const users = [
    { isHost: true, playerId: '3', isReady: true },
    { isHost: false, playerId: '1', isReady: false },
    { isHost: false, playerId: '1', isReady: true },
  ];

  // 고정된 길이의 배열 생성
  const userList = Array.from({ length: totalUsers }, (_, index) => {
    // 실제 유저 데이터로 대체
    return (
      users[index] || { isHost: false, playerId: undefined, isReady: false }
    );
  });

  return (
    <div className="waitingroom-brown-box flex flex-wrap justify-center gap-9">
      <div></div>

      {userList.map((user, index) => (
        <div key={index} className="flex-shrink-0">
          {/* playerId가 없을 경우에만 클릭 이벤트를 허용 */}
          {user.playerId ? (
            <RoomUserInfo
              isHost={user.isHost}
              playerId={user.playerId}
              isReady={user.isReady}
            />
          ) : (
            <button onClick={onOpen}>
              <RoomUserInfo
                isHost={user.isHost}
                playerId={user.playerId}
                isReady={user.isReady}
              />
            </button>
          )}
        </div>
      ))}
    </div>
  );
}
export default RoomUserList;
