import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';

interface GameChatProps {
  gameSocket: WebSocket | null;
  chatHistory: { playerId: string; message: string }[]; // 채팅 내역을 props로 받음
}

const GameChat: React.FC<GameChatProps> = ({ gameSocket, chatHistory }) => {
  const [message, setMessage] = useState('');
  const [isOpen, setIsOpen] = useState(true);

  // 게임 참가자들
  const participants = useSelector((state: RootState) => state.room.gameParticipants);

  // playerId를 닉네임으로 변환하는 함수
  const formatIdToNickname = (id: string) => {
    const participant = participants.find((p) => String(p.userId) === id); // userId를 string으로 변환하여 비교
    return participant ? participant.nickname : '알 수 없음';
  };

  // 메시지 보내기 핸들러
  const sendMessage = () => {
    if (gameSocket && message.trim()) {
      const chatMessage = {
        type: 'CHAT',
        message: message.trim(),
      };

      gameSocket.send(JSON.stringify(chatMessage));
      setMessage(''); // 메시지 전송 후 입력창 비우기
    }
  };

  // 엔터 키로 메시지 전송
  const handleKeyPress = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === 'Enter') {
      sendMessage();
    }
  };

  // 채팅창 열림/닫힘 토글
  const toggleChat = () => {
    setIsOpen(!isOpen);
  };

  return (
    <div className="fixed bottom-4 left-4 bg-[#e3d4b6] text-black p-4 rounded-lg shadow-lg w-[400px] h-auto z-50 border-[3px] border-[#8f6e3d]">
      {/* 채팅창 접기/펼치기 버튼 */}
      <div className="flex justify-between items-center mb-2">
        <h3 className="text-lg font-bold">게임 채팅</h3>
        <button
          onClick={toggleChat}
          className="bg-[#8f6e3d] text-white p-1 rounded-lg hover:bg-[#75532b]"
        >
          {isOpen ? '접기' : '펴기'}
        </button>
      </div>

      {isOpen && (
        <div className="flex flex-col h-full">
          {/* 채팅 내역 표시 */}
          <div className="flex-grow overflow-y-auto bg-[#f7e6c7] p-2 mb-2 rounded-lg h-[200px]">
            {chatHistory.map((chat, index) => (
              <div key={index} className="mb-1">
                {/* playerId를 닉네임으로 변환하여 출력 */}
                <span className="font-bold">{formatIdToNickname(chat.playerId)}: </span>
                <span>{chat.message}</span>
              </div>
            ))}
          </div>

          {/* 메시지 입력 및 전송 버튼 */}
          <div className="flex items-center">
            <input
              type="text"
              value={message}
              onChange={(e) => setMessage(e.target.value)}
              onKeyDown={handleKeyPress} // 엔터 키 이벤트 처리
              className="flex-grow p-2 bg-[#f7e6c7] text-black rounded-lg outline-none border-2 border-[#8f6e3d]"
              placeholder="메시지를 입력하세요"
            />
            <button
              onClick={sendMessage}
              className="ml-2 px-4 py-2 bg-[#8f6e3d] text-white rounded-lg hover:bg-[#75532b]"
            >
              전송
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default GameChat;
