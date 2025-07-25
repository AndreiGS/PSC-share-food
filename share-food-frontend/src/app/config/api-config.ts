export const environment = {
  production: false,
  baseUrl: 'https://www.foodtech.pw/share-food/api/v1'
};

export const API_ENDPOINTS = {
  AUTH: {
    OAUTH_CALLBACK: `${environment.baseUrl}/oauth/callback`,
    LOGOUT: `${environment.baseUrl}/auth/logout`
  },
  USER: {
    PROFILE: `${environment.baseUrl}/user/profile`
  },
  DONATION: {
    LIST: `${environment.baseUrl}/donation-requests/my-requests`,
    CREATE: `${environment.baseUrl}/donation-requests`,
    GET_BY_ID: (id: string) => `${environment.baseUrl}/donation-requests/${id}`
  }
};
