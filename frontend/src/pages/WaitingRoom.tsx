import { useEffect, useRef, useState } from 'react';
import axios from 'axios';
import { useNavigate, useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';

import { RootState } from '../redux/store';
import {
  setRoomId,
  setRoomName,
  setHostId,
  setGameParticipants,
  updateParticipantReadyState,
} from '../redux/slices/roomSlice';

import Modal from '../components/common/Modal';
import FriendInvite from '../components/waitingRoom/FriendInvite';
import MapInfo from '../components/waitingRoom/MapInfo';
import ReadyButton from '../components/waitingRoom/ReadyButton';
import RoomChat from '../components/waitingRoom/RoomChat';
import RoomHowTo from '../components/waitingRoom/RoomHowTo';
import RoomTitle from '../components/waitingRoom/RoomTitle';
import RoomUserList from '../components/waitingRoom/RoomUserList';
import { setCrops } from '../redux/slices/cropSlice';

interface GameParticipant {
  userId: number;
  isReady: boolean;
  nickname: string;
  description: string;
  characterName: string;
  tierPoint: number;
  mileage: number;
}

interface Message {
  type: string;
  content: {
    type: string;
    message: string;
    playerId: string;
  };
}

interface Player {
  playerId: number;
  ready: boolean;
}

function WaitingRoom() {
  const API_LINK = import.meta.env.VITE_API_URL;
  const SOCKET_LINK = import.meta.env.VITE_SOCKET_URL;
  const token = localStorage.getItem('access_token');
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const { roomId: roomIdFromParams } = useParams();
  const roomId = useSelector((state: RootState) => state.room.roomId);
  const hostId = useSelector((state: RootState) => state.room.hostId);
  const userId = useSelector((state: RootState) => state.user.id);
  const roomName = useSelector((state: RootState) => state.room.roomName);
  const gameParticipants = useSelector(
    (state: RootState) => state.room.gameParticipants,
  );

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [isAllReady, setIsAllReady] = useState(false);
  const [messages, setMessages] = useState<Message[]>([]);
  const socket = useRef<WebSocket | null>(null);

  // ❗❗❗❗❗❗❗❗ roomId 저장 useEffect ❗❗❗❗❗❗❗❗
  useEffect(() => {
    console.log(roomIdFromParams);
    if (roomIdFromParams) {
      dispatch(setRoomId(roomIdFromParams));
    }
  }, [roomIdFromParams, dispatch]);

  //❗❗❗❗❗❗❗❗useEffect❗❗❗❗❗❗❗❗
  useEffect(() => {
    // roomId가 없으면 실행 X
    if (!roomId) return;

    // ✅ 웹소켓 생성
    const newSocket = new WebSocket(
      `${SOCKET_LINK}/ws/game/${roomId}?playerId=${userId}`,
    );

    // ✅ 웹소켓 연결
    newSocket.onopen = async () => {
      console.log(
        '웹소켓 연결 성공 - roomId: ' + roomId + ', playerId: ' + userId,
      );
    };

    // ✅ 웹소켓 연결 오류 시 재연결 시도
    newSocket.onerror = (error) => {
      console.error('웹소켓 연결 오류: ', error);
      setTimeout(() => {
        console.log('재연결 시도 중...');
        socket.current = new WebSocket(
          `${SOCKET_LINK}/ws/game/${roomId}?playerId=${userId}`,
        );
        setIsLoading(false);
      }, 5000);
    };

    socket.current = newSocket;

    ///////////////////////////////////////
    // ✅소켓 메시지를 수신
    newSocket.onmessage = (event) => {
      // 메시지가 JSON 형식인지 확인
      if (event.data.startsWith('{') && event.data.endsWith('}')) {
        try {
          const receivedMessage = JSON.parse(event.data);
          console.log(receivedMessage);

          // 소켓에 수신된 메시지에 따라..
          switch (receivedMessage.type) {
            // ✅ 채팅
            case 'CHAT':
              setMessages((prevMessages) => [...prevMessages, receivedMessage]);
              break;
            // ✅ 공지 (입퇴장)
            case 'NOTIFY':
              // 누군가의 입장으로 방 정보 다시 가져오기
              fetchRoomData();
              console.log('누군가의 입장으로 정보 가져오기 함수 실행!');
              break;
            // ✅ 준비 / 준비완료
            case 'READY':
              const { readyStateList, allReady } = receivedMessage.content;

              // 배열의 각 요소를 순회하면서 상태를 확인
              readyStateList.forEach((player: Player) => {
                if (player.playerId !== hostId) {
                  console.log('일반 유저 준비 상태: ', player.ready);
                  dispatch(
                    updateParticipantReadyState({
                      playerId: player.playerId,
                      isReady: player.ready,
                    }),
                  );
                } else {
                  console.log('방장 준비 상태:', player.ready);
                }
              });
              setIsAllReady(allReady);
              console.log('모두 준비: ', allReady);
              break;
            // ✅ 게임 시작
            case 'ROUND_START': {
              const { duration, round } = receivedMessage.content;

              // 상태값이 잘 설정되었는지 확인
              console.log('Round Duration:', duration, 'Round Number:', round);

              // 페이지 이동 전 상태값 확인
              if (duration && round) {
                navigate(`/game/${roomId}`, {
                  state: { roundDuration: duration, roundNumber: round },
                });
              } else {
                alert('게임 시작에 필요한 정보가 부족합니다.');
              }
              break;
            }
            // ✅ 작물 리스트
            case 'CROP_LIST': {
              const { cropList } = receivedMessage.content;

              // cropList가 존재하는지 확인 후 상태 업데이트
              if (cropList && Array.isArray(cropList)) {
                dispatch(setCrops(cropList));
                console.log('Crop List Received:', cropList); // 데이터가 올바르게 수신되었는지 확인
              } else {
                console.error('Invalid Crop List:', cropList);
              }
              break;
            }
          }
        } catch (error) {
          console.error('JSON 파싱 오류:', error);
        }
      }
    };

    ///////////////////////////////////////
    // ✅ 게임방 정보 조회 API
    const fetchRoomData = async () => {
      try {
        const response = await axios.get(`${API_LINK}/room/info/${roomId}`);

        if (response.data && response.data.data) {
          const gameParticipants = response.data.data.participants;
          console.log('1번 - participants: ', gameParticipants);

          const formattedParticipants = Object.entries(gameParticipants).map(
            ([userId, isReady]) => ({
              userId: Number(userId), // userId는 숫자로 변환
              isReady: Boolean(isReady), // 준비 상태 Boolean으로 변환 (방장은 항상 true)
              nickname: '',
              description: '',
              characterName: '',
              tierPoint: 0,
              mileage: 0,
            }),
          );
          console.log('2번 - formattedParticipants: ', formattedParticipants);

          const fetchUsers = await Promise.all(
            formattedParticipants.map(
              async (gameParticipant: GameParticipant) => {
                const userId = gameParticipant.userId;
                const userResponse = await axios.get(
                  `${API_LINK}/user/${userId}`,
                  {
                    headers: {
                      Authorization: `Bearer ${token}`,
                    },
                  },
                );

                return {
                  ...gameParticipant, // 기존 정보 유지
                  nickname: userResponse.data.data.nickname || '', // 새로 가져온 정보 병합
                  description: userResponse.data.data.description || '',
                  characterName: userResponse.data.data.characterName || '',
                  tierPoint: userResponse.data.data.tierPoint || 0,
                  mileage: userResponse.data.data.mileage || 0,
                };
              },
            ),
          );

          dispatch(setGameParticipants(fetchUsers));
          dispatch(setRoomName(response.data.data.roomName));
          dispatch(setHostId(response.data.data.hostId));
          console.log(
            '3번 - GameParticipants에 저장할 fetchUsers: ',
            fetchUsers,
          );
        } else {
          console.error('응답 데이터에 participants가 없습니다.');
        }
      } catch (error) {
        console.error('방 정보 가져오는 중 에러 발생: ', error);
      }
    };

    // 🔵 방 정보 가져오기 함수 실행
    fetchRoomData();

    ///////////////////////////////////////
    // ✅ 3초 뒤에 로딩 끝내기 (입장 중 화면)
    const delay = setTimeout(() => {
      if (isLoading) {
        setIsLoading(false);
      }
    }, 3000);

    ///////////////////////////////////////
    // ✅ 방 퇴장 시 방 퇴장 및 소켓 종료 & 게임방 정보 새로 가져오기(방장, 리스트 바뀜)
    // 뒤로가기, 새로고침, 창닫기, 탭닫기 시 방 떠나기
    // 페이지 떠날 때 경고 알림창
    const handleBeforeUnload = async (event: BeforeUnloadEvent) => {
      event.preventDefault();
      event.returnValue = '';

      try {
        // 방 퇴장 API 호출
        const response = await axios.post(`${API_LINK}/room/exit/${roomId}`, {
          playerId: userId,
        });
        if (response.status === 200) {
          console.log('방 퇴장 완료');

          // 방장 위임 처리 확인
          if (response.data.data.hostId !== userId) {
            dispatch(setHostId(response.data.data.hostId));
            console.log('방장이 위임되었습니다:', response.data.data.hostId);
          } else {
            // 방장이 위임되지 않은 경우
            alert('방장이 없습니다.');
            navigate('/lobby');
          }
        }
      } catch (error: unknown) {
        if (axios.isAxiosError(error)) {
          if (error.response && error.response.status === 404) {
            alert('방을 찾을 수 없습니다.');
            navigate('/lobby');
          } else {
            console.error('방 정보 가져오는 중 에러 발생: ', error);
          }
        } else {
          console.error('예상치 못한 오류 발생: ', error);
        }
      }
    };

    // 🔵 이벤트 리스너 추가
    window.addEventListener('beforeunload', handleBeforeUnload);

    // ✅ 컴포넌트 언마운트 시 타이머 및 웹소켓 연결 정리
    return () => {
      clearTimeout(delay);
      // 현재 페이지가 game 페이지로 이동하는 경우가 아니면 웹소켓 닫기
      if (!window.location.pathname.startsWith(`/game/${roomId}`)) {
        if (socket.current) {
          socket.current.close();
          console.log('웹소켓 연결 닫기');
        }
      }
      // 이벤트 리스너 제거
      window.removeEventListener('beforeunload', handleBeforeUnload);
    };
  }, [roomId, userId, hostId]);

  const handleOpenModal = () => setIsModalOpen(true); // 모달 열기
  const handleCloseModal = () => setIsModalOpen(false); // 모달 닫기

  return (
    <>
      {/* <Header /> */}
      <div className="flex relative w-[1200px] justify-center mx-auto">
        {isLoading && (
          <Modal>
            <div className="text-center">
              <p className="white-title">방 들어가는 중...</p>
            </div>
          </Modal>
        )}

        {/* 왼쪽 섹션 (방제목/게임 참여자 리스트/채팅) */}
        <section className="w-2/3 mr-4">
          <RoomTitle roomName={roomName} roomId={roomId} />
          {/* 게임 참여 대기자 리스트 */}
          <RoomUserList onOpen={handleOpenModal} socket={socket.current} />
          <RoomChat messages={messages} socket={socket.current} />
        </section>

        {/* 오른쪽 섹션 (맵/게임방법/준비버튼) */}
        <section className="w-1/3">
          <MapInfo />
          <RoomHowTo />
          <ReadyButton socket={socket.current} isAllReady={isAllReady} />
        </section>

        {isModalOpen && (
          <Modal>
            <FriendInvite onClose={handleCloseModal} />
          </Modal>
        )}
      </div>
    </>
  );
}

export default WaitingRoom;
