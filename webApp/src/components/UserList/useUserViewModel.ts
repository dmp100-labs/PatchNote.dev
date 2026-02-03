import { useState, useCallback } from 'react';
import { UserBridge } from 'shared'; // KMP Bridge
import { UserUiState, initialUserUiState } from './UserUiState';

export function useUserViewModel() {
  const [uiState, setUiState] = useState<UserUiState>(initialUserUiState);

  const loadUsers = useCallback(async () => {
    setUiState(prev => ({ ...prev, isLoading: true, error: null }));

    try {
      const bridge = new UserBridge();
      const result = await bridge.getUsers();

      setUiState(prev => ({
        ...prev,
        isLoading: false,
        users: Array.from(result)
      }));

    } catch (e: any) {
      setUiState(prev => ({
        ...prev,
        isLoading: false,
        error: e.message || "알 수 없는 에러가 발생했습니다."
      }));
    }
  }, []);

  return {
    uiState,
    loadUsers
  };
}