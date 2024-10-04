export const handleSocketMessage = (
  event: MessageEvent,
  setMessages?: React.Dispatch<React.SetStateAction<any[]>>,
  onParticipantUpdate?: (participants: any[]) => void,
) => {
  const data = event.data;

  // 메시지가 JSON 형식인지 확인
  if (data.startsWith('{') && data.endsWith('}')) {
    try {
      const receivedMessage = JSON.parse(data);

      if (setMessages && receivedMessage.type === 'CHAT') {
        setMessages((prevMessages) => [...prevMessages, receivedMessage]);
      } else if (onParticipantUpdate && receivedMessage.type === 'READY') {
        onParticipantUpdate(receivedMessage.content.participants);
      }
    } catch (error) {
      console.error('JSON 파싱 오류:', error);
    }
  }
};
