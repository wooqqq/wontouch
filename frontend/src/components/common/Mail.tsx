import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';
import { useEffect } from 'react';
import axios from 'axios';
import Modal from './Modal';

import mail from '../../assets/icon/mail.png';
import cancel from '../../assets/icon/cancel.png';
import confirm from '../../assets/icon/confirm.png';
import boy from '../../assets/background/characters/stand/boy.png';
import curlyhairBoy from '../../assets/background/characters/stand/curlyhair_boy.png';
import flowerGirl from '../../assets/background/characters/stand/flower_girl.png';
import girl from '../../assets/background/characters/stand/girl.png';
import goblin from '../../assets/background/characters/stand/goblin.png';
import kingGoblin from '../../assets/background/characters/stand/king_goblin.png';
import ninjaSkeleton from '../../assets/background/characters/stand/ninja_skeleton.png';
import skeleton from '../../assets/background/characters/stand/skeleton.png';

// 이미지 매핑 객체 생성
const characterImages: { [key: string]: string } = {
  boy: boy,
  curlyhair_boy: curlyhairBoy,
  flower_girl: flowerGirl,
  girl: girl,
  goblin: goblin,
  king_goblin: kingGoblin,
  ninja_skeleton: ninjaSkeleton,
  skeleton: skeleton,
};

interface Notification {
  id: string;
  senderId: number;
  senderNickname: string;
  notificationType: string;
  content: string;
}

interface requestInfo {
  fromUserId: number;
  fromUserNickname: string;
  fromUserTierPoint: number;
  fromUserCharacterName: string;
}

export default function Mail({
  closeMail,
  setNotificationCount,
}: {
  closeMail: () => void;
  setNotificationCount: (count: number) => void;
}) {
  const API_LINK = import.meta.env.VITE_API_URL;

  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [selectedNotification, setSelectedNotification] =
    useState<Notification | null>(null);
  const [requestInfo, setRequestInfo] = useState<requestInfo>();
  const [showModal, setShowModal] = useState(false);

  const userId = useSelector((state: RootState) => state.user.id);
  const accessToken = localStorage.getItem('access_token');

  // 알림 불러오기
  const getNotifications = async () => {
    try {
      const response = await axios.get(
        `${API_LINK}/notification/list/${userId}`,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        },
      );

      // 데이터가 있으면 state 변경
      const data = response.data.data;
      console.log(data);
      if (data) {
        setNotifications(data);
        setNotificationCount(data.length);
      }
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    getNotifications();
  }, []);

  // 친구 요청 수락
  const acceptFriendRequest = async (
    senderId: number,
    notificationId: string,
  ) => {
    try {
      await axios.post(
        `${API_LINK}/friend/request-accept`,

        {
          userId: userId,
          fromUserId: senderId,
          notificationId: notificationId,
        },
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        },
      );
      alert('친구 요청을 수락했습니다!');
      setShowModal(false);
      getNotifications();
    } catch (error) {
      console.log(error);
    }
  };

  // 친구 요청 거절
  const rejectFriendRequest = async (
    senderId: number,
    notificationId: string,
  ) => {
    try {
      await axios.post(
        `${API_LINK}/friend/request-reject`,

        {
          userId: userId,
          fromUserId: senderId,
          notificationId: notificationId,
        },
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        },
      );
      alert('친구 요청을 거절했습니다!');
      setShowModal(false);
      getNotifications();
    } catch (error) {
      console.log(error);
    }
  };

  // 알림 클릭 시 모달 열기
  const handleNotificationClick = (notification: Notification) => {
    setSelectedNotification(notification);
    getRequestInfo(notification.senderId);
    setShowModal(true);
  };

  // 요청 상세 보기
  const getRequestInfo = async (senderId: number) => {
    try {
      const response = await axios.get(`${API_LINK}/friend/request/detail`, {
        params: {
          fromUserId: senderId,
          toUserId: userId,
        },
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
      console.log(response.data.data);
      setRequestInfo(response.data.data);
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="yellow-box w-1/2 h-[470px] p-6 px-10 pb-10 border-[#36EAB5] bg-[#fffeee]">
      <div className="relative mb-4">
        <div className="flex justify-center mint-title text-5xl">메일함</div>
        <button
          className="absolute right-0 top-1/2 transform -translate-y-1/2"
          onClick={closeMail}
        >
          <img src={cancel} alt="메일함 닫기" />
        </button>
      </div>

      <div className="list-box h-5/6 p-4">
        {/* 알림 데이터가 있을 경우 렌더링 */}
        {notifications.length > 0 ? (
          notifications.map((notification) => (
            <button
              className="flex p-4 relative"
              key={notification.id}
              onClick={() => handleNotificationClick(notification)}
            >
              <div>
                <img src={mail} alt="" />
              </div>
              <div className="flex items-center white-text text-xl mx-5">
                {notification.content}
              </div>
            </button>
          ))
        ) : (
          <div className="white-text text-3xl">받은 알림이 없습니다.</div>
        )}
      </div>

      {/* 모달 */}
      {showModal && selectedNotification && (
        <Modal>
          {selectedNotification.notificationType === 'FRIEND_REQUEST' ? (
            <div className="yellow-box w-1/2 h-[400px] p-6 border-[#36EAB5] bg-[#fffeee]">
              <div className="mint-title mb-5">친구 요청</div>
              <div className="mb-5 flex justify-center items-center">
                <div className="friend-search-box w-36 h-36 rounded-full mr-10">
                  {requestInfo?.fromUserCharacterName && (
                    <img
                      src={characterImages[requestInfo.fromUserCharacterName]}
                      alt=""
                      className="p-6"
                    />
                  )}
                </div>
                <div className="friend-search-box w-56 h-[100px] rounded-3xl flex items-center justify-center">
                  {requestInfo?.fromUserNickname && (
                    <div>
                      <div>남작</div>
                      <div className="white-text text-2xl">
                        {requestInfo.fromUserNickname}
                      </div>
                    </div>
                  )}
                </div>
              </div>

              <div className="white-text text-3xl mb-8">
                친구 요청을 수락하시겠어요?
              </div>
              <div className="flex justify-between px-60">
                <button
                  onClick={() =>
                    rejectFriendRequest(
                      selectedNotification.senderId,
                      selectedNotification.id,
                    )
                  }
                >
                  <img src={cancel} alt="" />
                </button>
                <button
                  onClick={() =>
                    acceptFriendRequest(
                      selectedNotification.senderId,
                      selectedNotification.id,
                    )
                  }
                >
                  <img src={confirm} alt="" />
                </button>
              </div>
            </div>
          ) : (
            <div>게임 초대..</div>
          )}
        </Modal>
      )}
    </div>
  );
}
