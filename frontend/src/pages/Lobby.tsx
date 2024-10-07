import { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { setNotificationCount } from '../redux/slices/notificationSlice';
import axios from 'axios';
import Modal from '../components/common/Modal';
import MakeRoom from '../components/lobby/room/MakeRoom';
import FindRoom from '../components/lobby/room/FindRoom';
import Header from '../components/common/Header';
import Ranking from '../components/lobby/ranking/Ranking';
import RoomList from '../components/lobby/room/RoomList';
import Friend from '../components/lobby/friend/Friend';

import search from '../assets/icon/search.png';
import hammer from '../assets/icon/hammer.png';
import { RootState } from '../redux/store';

const API_LINK = import.meta.env.VITE_API_URL;

function Lobby() {
  const [showMakeRoom, setShowMakeRoom] = useState<boolean>(false);
  const [showFindRoom, setShowFindRoom] = useState<boolean>(false);

  const dispatch = useDispatch(); // dispatch 훅 사용
  const notificationCount = useSelector(
    (state: RootState) => state.notification.count,
  ); // Redux에서 알림 수 가져오기

  const userId = useSelector((state: RootState) => state.user.id);
  const accessToken = localStorage.getItem('access_token');

  // SSE 연결
  const setupSSE = async (userId: number, accessToken: string) => {
    try {
      const eventSource = new EventSource(
        `${API_LINK}/notification/subscribe/${userId}`,
      );

      // 알림 수신 시 api 호출
      eventSource.onmessage = async (event) => {
        console.log(event.data);
        await getNotifications(userId, accessToken);
      };
    } catch (error) {
      console.log(error);
    }
  };

  // 알림 목록 호출
  const getNotifications = async (userId: number, accessToken: string) => {
    try {
      const response = await axios.get(
        `${API_LINK}/notification/list/${userId}`,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        },
      );

      const data = response.data.data;
      if (data) {
        console.log(data.length);
        dispatch(setNotificationCount(data.length)); // Redux 액션으로 알림 수 업데이트
      }
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    if (userId && accessToken) {
      setupSSE(userId, accessToken);
      getNotifications(userId, accessToken); // 초기 알림 목록 호출
    }
  }, [userId, accessToken]);

  // 방 생성 모달
  const openMakeRoom = () => setShowMakeRoom(true);
  const closeMakeRoom = () => setShowMakeRoom(false);

  // 방 찾기 모달
  const openFindRoom = () => setShowFindRoom(true);
  const closeFindRoom = () => setShowFindRoom(false);

  return (
    <div>
      <Header notificationCount={notificationCount} />
      <div className="flex justify-between">
        <div className="yellow-box w-8/12 h-5/6 flex flex-col justify-center p-2 px-6 ml-10">
          <div className="flex space-x-4 mb-4">
            <div>
              <button
                onClick={openMakeRoom}
                className="ready-button w-52 h-14 text-3xl"
              >
                방 만들기
                <img src={hammer} alt="Create Room" />
              </button>
            </div>
            <div>
              <button
                onClick={openFindRoom}
                className="ready-button w-52 h-14 text-3xl"
              >
                방 찾기
                <img src={search} alt="Find Room" />
              </button>
            </div>
          </div>
          <div className="list-box overflow-auto flex flex-wrap justify-between p-2 w-full h-[530px]">
            <RoomList />
          </div>
        </div>
        <div className="ranking-container w-4/12 h-5/6 flex flex-col items-center">
          <div className="w-11/12 ml-6 mb-6 h-[400px]">
            <Ranking />
          </div>
          <div className="w-11/12 h-[218px] ml-6">
            <Friend />
          </div>
        </div>
      </div>

      {showMakeRoom && (
        <Modal>
          <MakeRoom closeMakeRoom={closeMakeRoom} />
        </Modal>
      )}

      {showFindRoom && (
        <Modal>
          <FindRoom closeFindRoom={closeFindRoom} />
        </Modal>
      )}
    </div>
  );
}

export default Lobby;
