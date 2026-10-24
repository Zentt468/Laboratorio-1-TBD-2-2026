import React from 'react';
import piggyLogo from '../assets/piggy.svg';

export default function Navbar({
  vistaActual, setVistaActual, usuarioActual, onLogin, onLogout
}) {
  const tieneRol = (rol) => usuarioActual?.roles?.includes(rol);

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-white border-bottom sticky-top py-2 px-3 px-md-5">
      <div className="container-fluid">
        {/*Logo y nombre a la izquierda*/}
        <button 
            className="navbar-brand btn btn-link text-decoration-none p-0 fw-bold fs-2" 
            style={{ color: '#1668e3', fontStyle: 'italic', letterSpacing: '-1px' }}
            onClick={() => setVistaActual('home')}
        >
            <img 
                src={piggyLogo} 
                alt="Logo" 
                style={{ width: '38px', height: '38px', objectFit: 'contain' }} 
            />
            <span 
                className="fw-bold fs-2" 
                style={{ color: '#1668e3', fontStyle: 'italic', paddingLeft: '5px' }}
            >
                Mish
            </span>
        </button>

        {/*Botones de navegacion a la derecha*/}
        <div className="d-flex align-items-center gap-3">
          {tieneRol('ROLE_ANFITRION') && (
            <button 
              className={`btn btn-link text-decoration-none text-dark fw-semibold p-0 ${
                vistaActual === 'publicar' ? 'border-bottom border-primary border-2' : ''
              }`}
              onClick={() => setVistaActual('publicar')}
            >
              Publicar propiedad
            </button>
          )}

          {usuarioActual && (
          <button 
            className={`btn btn-link text-decoration-none text-dark fw-semibold p-0 ${
              vistaActual === 'viajes' ? 'border-bottom border-primary border-2' : ''
            }`}
            onClick={() => setVistaActual('viajes')}
          >
            Mis viajes
          </button>
          )}

          {usuarioActual ? (
            <div className="d-flex align-items-center gap-2 ms-2">
              <span className="small fw-semibold text-secondary d-flex align-items-center gap-1">
                <i className="bi bi-person-circle fs-5 text-primary"></i>
                {usuarioActual.nombre}
              </span>
              <button
                className="btn btn-outline-secondary btn-sm rounded-pill px-3"
                onClick={onLogout}
              >
                Cerrar sesión
              </button>
            </div>
          ) : (
            <button
              className="btn btn-outline-primary rounded-pill px-3 py-1 fw-semibold d-flex align-items-center gap-2"
              onClick={onLogin}
            >
              <i className="bi bi-person-circle fs-5"></i>
              <span>Iniciar sesión</span>
            </button>
          )}
        </div>
      </div>
    </nav>
  );
}