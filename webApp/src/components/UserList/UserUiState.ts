import { JsUser } from 'shared';

export interface UserUiState {
  users: JsUser[];
  isLoading: boolean;
  error: string | null;
}

export const initialUserUiState: UserUiState = {
  users: [],
  isLoading: false,
  error: null,
};