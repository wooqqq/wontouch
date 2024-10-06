import { useEffect, useRef, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';

import { RootState } from '../redux/store';
import {
  setRoomId,
  setRoomName,
  setHostId,
  setGameParticipants,
} from '../redux/slices/roomSlice';

import Modal from '../components/common/Modal';
import FriendInvite from '../components/waitingRoom/FriendInvite';
import MapInfo from '../components/waitingRoom/MapInfo';
import ReadyButton from '../components/waitingRoom/ReadyButton';
import RoomChat from '../components/waitingRoom/RoomChat';
import RoomHowTo from '../components/waitingRoom/RoomHowTo';
import RoomTitle from '../components/waitingRoom/RoomTitle';
import RoomUserList from '../components/waitingRoom/RoomUserList';

interface Message {
  type: string;
  content: {
    type: string;
    message: string;
    playerId: string;
  };
}

function WaitingRoom() {
  const API_LINK = import.meta.env.VITE_API_URL;
  const token = localStorage.getItem('access_token');
  const dispatch = useDispatch();

  const { roomId: roomIdFromParams } = useParams();
  const roomId = useSelector((state: RootState) => state.room.roomId);
  const hostId = useSelector((state: RootState) => state.room.hostId);
  const playerId = useSelector((state: RootState) => state.user.id);
  const roomName = useSelector((state: RootState) => state.room.roomName);
  const gameParticipants = useSelector(
    (state: RootState) => state.room.gameParticipants,
  );

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [isReady, setIsReady] = useState(false);
  const [isAllReady, setIsAllReady] = useState(false);
  const [users, setUsers] = useState<any[]>([]);
  const [messages, setMessages] = useState<Message[]>([]);
  const socket = useRef<WebSocket | null>(null);

  // roomId 저장
  useEffect(() => {
    console.log(roomIdFromParams);
    if (roomIdFromParams) {
      dispatch(setRoomId(roomIdFromParams));
    }
  }, [roomIdFromParams, dispatch]);

  ///////////////useEffect//////////////
  useEffect(() => {
    // roomId가 없으면 실행 X
    if (!roomId) return;

    // 웹소켓 생성
    const newSocket = new WebSocket(
      `ws://localhost:8082/socket/ws/game/${roomId}?playerId=${playerId}`,
    );

    // 웹소켓 연결
    newSocket.onopen = async () => {
      console.log(
        '웹소켓 연결 성공 - roomId: ' + roomId + ', playerId: ' + playerId,
      );
    };

    // 웹소켓 연결 오류 시 재연결 시도
    newSocket.onerror = (error) => {
      console.error('웹소켓 연결 오류: ', error);
      setTimeout(() => {
        console.log('재연결 시도 중...');
        socket.current = new WebSocket(
          `ws://localhost:8082/socket/ws/game/${roomId}?playerId=${playerId}`,
        );
        setIsLoading(false);
      }, 5000);
    };

    socket.current = newSocket;

    ///////////////////////////////////////
    // 소켓에서 메시지를 수신할 때
    newSocket.onmessage = (event) => {
      // 메시지가 JSON 형식인지 확인
      if (event.data.startsWith('{') && event.data.endsWith('}')) {
        try {
          const receivedMessage = JSON.parse(event.data);
          console.log(receivedMessage);

          switch (receivedMessage.type) {
            case 'CHAT':
              setMessages((prevMessages) => [...prevMessages, receivedMessage]);
              break;
            case 'NOTIFY':
              // 누군가의 입장
              console.log('누군가의 입장으로 1초 뒤에 정보 가져오기');

              // 1초 뒤에 정보 다시 가져오기
              setTimeout(() => {
                fetchRoomData();
              }, 1000);
              break;
            case 'READY':
              // 응답에서 플레이어 준비 상태 업데이트
              if (receivedMessage.content.playerId === playerId) {
                if (
                  receivedMessage.content.playerId === hostId &&
                  receivedMessage.content.ready === false
                ) {
                  // 보낸 사람의 아이디가 호스트아이디이고, 레디가 안된 상태면 강제 레디
                  hostReady();
                }
                setIsReady(receivedMessage.content.ready);
                setIsAllReady(receivedMessage.content.allReady);
                console.log('준비: ', receivedMessage.content.ready);
                console.log('모두 준비: ', receivedMessage.content.allReady);
              }
          }
        } catch (error) {
          console.error('JSON 파싱 오류:', error);
        }
      }
    };

    ///////////////////////////////////////
    // 게임방 정보 가져오기 (방 입장 API)
    const fetchRoomData = async () => {
      try {
        const response = await axios.post(`${API_LINK}/room/join/${roomId}`, {
          playerId: playerId,
        });
        if (response.data && response.data.data) {
          const gameParticipants = response.data.data.participants;
          const formattedParticipants = Object.entries(gameParticipants).map(
            ([userId, isReady]) => ({
              userId: Number(userId), // userId는 숫자로 변환
              isReady: Boolean(isReady), // 준비 상태 Boolean으로 변환
              nickname: '',
              description: '',
              characterName: '',
              tierPoint: 0,
              mileage: 0,
            }),
          );

          dispatch(setGameParticipants(formattedParticipants));
          dispatch(setRoomName(response.data.data.roomName));
          dispatch(setHostId(response.data.data.hostId));

          console.log('1번-방정보 가져오기', formattedParticipants);

          // 방 정보 토대로 유저 가져오기
          fetchUsersInfo();
          // 호스트 레디상태 변경
          hostReady();
        } else {
          console.error('응답 데이터에 participants가 없습니다.');
        }
      } catch (error) {
        console.error('방 정보 가져오는 중 에러 발생: ', error);
      }
    };
    // 방 정보 가져오기
    fetchRoomData();

    ///////////////////////////////////////
    // 방장id와 유저 id가 같으면 방장은 알아서 ready준비가 되게 전송
    const hostReady = () => {
      if (hostId === playerId) {
        const readyRequest = {
          type: 'READY',
        };
        // ready 전송
        socket.current?.send(JSON.stringify(readyRequest));
      }
    };

    ///////////////////////////////////////
    // 참가자 정보 가져오기 API
    const fetchUsersInfo = async () => {
      try {
        // 기존 유저 데이터 초기화
        setUsers([]);

        // participants가 존재하는지 체크
        if (!gameParticipants || gameParticipants.length === 0) return;

        const fetchUsers = await Promise.all(
          gameParticipants.map(async (gameParticipant) => {
            const userId = gameParticipant.userId;
            const userResponse = await axios.get(`${API_LINK}/user/${userId}`, {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            });
            return userResponse.data.data;
          }),
        );
        setUsers(fetchUsers);
        dispatch(setGameParticipants(fetchUsers));
        console.log('2번-방정보 토대로 유저 정보 가져오기');
      } catch (error) {
        console.error('유저 정보를 가져오는 중 오류 발생: ', error);
      }
    };

    ///////////////////////////////////////
    // 방 퇴장 시 게임방 정보 새로 가져오기(방장, 리스트 바뀜)

    ///////////////////////////////////////
    // 3초 뒤에 로딩 끝내기
    const delay = setTimeout(() => {
      if (isLoading) {
        setIsLoading(false);
      }
    }, 3000);

    ///////////////////////////////////////
    // 페이지 떠날 때 경고 알림창 추가
    const handleBeforeUnload = (event: BeforeUnloadEvent) => {
      event.preventDefault();
      event.returnValue = '';
    };

    // 이벤트 리스너 추가
    window.addEventListener('beforeunload', handleBeforeUnload);

    // 컴포넌트 언마운트 시 타이머 및 웹소켓 연결 정리
    return () => {
      clearTimeout(delay);
      if (socket.current) {
        socket.current.close();
        console.log('웹소켓 연결 닫기');
      }
      // 컴포넌트 언마운트 시 이벤트 리스너 제거
      window.removeEventListener('beforeunload', handleBeforeUnload);
    };
  }, [roomId, playerId, hostId]);

  const handleOpenModal = () => setIsModalOpen(true); // 모달 열기
  const handleCloseModal = () => setIsModalOpen(false); // 모달 닫기

  return (
    <div className="flex relative">
      {isLoading && (
        <Modal>
          <div className="text-center">
            <p className="white-title">방 들어가는 중...</p>
          </div>
        </Modal>
      )}

      {/* 왼쪽 섹션 (방제목/게임 참여자 리스트/채팅) */}
      <section className="w-2/3">
        <RoomTitle roomName={roomName} roomId={roomId} />
        {/* 게임 참여 대기자 리스트 */}
        <RoomUserList
          onOpen={handleOpenModal}
          socket={socket.current}
          users={users}
        />
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
  );
}

export default WaitingRoom;
