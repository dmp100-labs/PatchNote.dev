import React from 'react';
import './UserList.css';
import { useUserViewModel } from './useUserViewModel';

export function UserList() {
  const { uiState, loadUsers } = useUserViewModel();

  return (
    <div className="user-list-container">
      <h2>유저 목록 (MVVM)</h2>
      <button
        onClick={loadUsers}
        disabled={uiState.isLoading}
        className="load-button"
      >
        {uiState.isLoading ? "로딩 중..." : "유저 불러오기"}
      </button>
      <div className="content-area">
        {uiState.error && (
          <div className="error-msg">🚨 에러: {uiState.error}</div>
        )}

        {!uiState.isLoading && !uiState.error && uiState.users.length === 0 && (
          <p className="empty-msg">버튼을 눌러 데이터를 불러오세요.</p>
        )}

        <div className="list">
          {uiState.users.map((user) => (
            <div key={user.id} className="user-card">
              <div className="user-name">{user.name}</div>
              <div className="user-email">{user.email}</div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}