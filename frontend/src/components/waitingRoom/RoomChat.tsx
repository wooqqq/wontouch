import { useEffect, useRef, useState } from 'react';
import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';

interface Message {
  type: string;
  content: {
    type: string;
    message: string;
    playerId: string;
  };
}

interface RoomInfoProps {
  roomId: string | undefined;
  participants: string[];
  socket: WebSocket | null;
}

function RoomChat({ socket }: RoomInfoProps) {
  const userId = useSelector((state: RootState) => state.user.id);
  const [message, setMessage] = useState('');
  const [messages, setMessages] = useState<Message[]>([]);
  const messagesEndRef = useRef<HTMLDivElement | null>(null);

  // 스크롤 맨 밑으로 내리기
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  useEffect(() => {
    if (!socket) return;

    // 서버로부터 메시지가 도착했을 때 호출
    socket.onmessage = (event) => {
      const data = event.data;

      // 메시지가 JSON 형식인지 확인
      if (data.startsWith('{') && data.endsWith('}')) {
        try {
          const receivedMessage = JSON.parse(data);
          // console.log('받은 메시지:', receivedMessage);
          setMessages((prevMessages) => [...prevMessages, receivedMessage]);
        } catch (error) {
          console.error('JSON 파싱 오류:', error);
        }
      } else {
        // 일반 문자열 메시지 처리
        // console.log('받은 일반 메시지:', data);
        setMessages((prevMessages) => [
          ...prevMessages,
          { type: 'NOTIFY', content: data },
        ]);
      }
    };
    // 컴포넌트가 언마운트될 때 이벤트 리스너를 제거
    return () => {
      socket.onmessage = null;
    };
  }, [socket]);

  // 메시지 전송
  const handleSendMessage = (e: React.FormEvent) => {
    // 자동 전송 방지
    e.preventDefault();
    if (!socket || !message.trim()) return;

    const chatMessage = {
      type: 'CHAT',
      message: message,
      playerId: userId,
    };

    socket.send(JSON.stringify(chatMessage));
    setMessage(''); // 메시지 전송 후 입력창 초기화
  };

  return (
    <div className="waitingroom-brown-box py-0 pl-3 h-[180px]">
      <section className="font-galmuri11 h-[70%] mt-2 mr-2 pl-2 pb-2 overflow-scroll overflow-x-hidden">
        {messages.map((msg, index) => (
          <div key={index}>
            {msg.type == 'NOTIFY' ? (
              <p className="text-[#10ab7d]">{msg.content.message}</p>
            ) : (
              <p>
                {msg.content.playerId} : {msg.content.message}
              </p>
            )}
            <div ref={messagesEndRef} />
          </div>
        ))}
      </section>

      <form onSubmit={handleSendMessage}>
        <input
          type="text"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          placeholder="메시지를 입력하세요"
          className="bg-[#FFF2D1] font-galmuri11 rounded-[10px] p-2 w-4/5 focus:outline-none"
        />
        <button type="submit">전송</button>
      </form>
    </div>
  );
}

export default RoomChat;
