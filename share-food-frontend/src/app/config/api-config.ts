export const environment = {
  production: false,
  baseUrl: 'http://localhost:8080/share-food/api/v1'
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
    LIST: `${environment.baseUrl}/donations`,
    CREATE: `${environment.baseUrl}/donations`,
    GET_BY_ID: (id: string) => `${environment.baseUrl}/donations/${id}`
  }
};
