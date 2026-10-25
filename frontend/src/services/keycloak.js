import Keycloak from 'keycloak-js';

const keycloakConfig = {
  url: 'http://localhost:8080',
  realm: 'alojamientos-realm',
  clientId: 'alojamientos-frontend',
};

const keycloak = new Keycloak(keycloakConfig);

export default keycloak;