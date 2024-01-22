import axios from 'axios';

// Actions

export const toggleLock = () => ({
  type: 'updateLock',
  payload: axios.post('api/miscellaneous/toggle-billing-plan-lock'),
  meta: {
    successMessage: '<strong>Lock changed!</strong>',
    errorMessage: '<strong>An error has occurred!</strong> The lock could not be changed.'
  }
});

export function getLockStatus() {
  const res = axios.get('api/miscellaneous/get-lock-status').then(res => {
    return res.data as boolean;
  });

  return res;
}

export const getLockComments = () => ({
  type: 'getLockComments',
  payload: axios.get('api/miscellaneous/get-lock-billing-plan-comments'),
  meta: {
    successMessage: '<strong>Lock comments retrieved!</strong>',
    errorMessage: '<strong>An error has occurred!</strong> The lock comments could not be retrieved.'
  }
});

export const setLockComments = (comments: string) => ({
  type: 'updateLockComments',
  payload: axios({
    method: 'post',
    url: 'api/miscellaneous/set-lock-billing-plan-comments',
    headers: { 'Content-Type': 'text/plain' },
    data: {
      body: comments
    }
  }),
  meta: {
    successMessage: '<strong>Lock comments updated!</strong>',
    errorMessage: '<strong>An error has occurred!</strong> The lock comments could not be updated.'
  }
});
