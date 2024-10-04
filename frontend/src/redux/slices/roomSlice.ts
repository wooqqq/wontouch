import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface Participant {
  id: number;
  nickname: string;
}

interface RoomState {
  roomId: string | '';
  roomName: string | '';
  HostId: number | null;
  participants: Participant[];
  currentPlayerCount: number | 0;
  isPrivate: boolean;
  password: string | '';
}

const initialState: RoomState = {
  roomId: '',
  roomName: '',
  HostId: null,
  participants: [],
  currentPlayerCount: 0,
  isPrivate: false,
  password: '',
};

const roomSlice = createSlice({
  name: 'room',
  initialState,
  reducers: {
    setRoomId(state, action: PayloadAction<string | ''>) {
      state.roomId = action.payload;
    },
    setRoomName(state, action: PayloadAction<string | ''>) {
      state.roomName = action.payload;
    },
    setHostId(state, action: PayloadAction<number | 0>) {
      state.HostId = action.payload;
    },
    setParticipants(state, action: PayloadAction<Participant[]>) {
      state.participants = action.payload;
    },
    addParticipant(state, action: PayloadAction<Participant>) {
      state.participants.push(action.payload);
      state.currentPlayerCount = state.participants.length; // 참여자 수 업데이트
    },
    removeParticipant(state, action: PayloadAction<number>) {
      state.participants = state.participants.filter(
        (participant) => participant.id !== action.payload,
      );
      state.currentPlayerCount = state.participants.length; // 참여자 수 업데이트
    },
    setIsPrivate(state, action: PayloadAction<boolean>) {
      state.isPrivate = action.payload;
    },
    setPassword(state, action: PayloadAction<string | ''>) {
      state.password = action.payload;
    },
  },
});

export const {
  setRoomId,
  setRoomName,
  setHostId,
  setParticipants,
  addParticipant,
  removeParticipant,
  setIsPrivate,
  setPassword,
} = roomSlice.actions;
export default roomSlice.reducer;
