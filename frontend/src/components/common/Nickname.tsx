import React from 'react';
import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';

export default function Nickname() {
  const userNickname = useSelector((state: RootState) => state.user.nickname);

  return <div>{userNickname}</div>;
}
