import React, { useState, useEffect } from 'react';
import Navbar from './components/Navbar';
import HeroSearch from './components/HeroSearch';
import AlojamientoCard from './components/AlojamientoCard';
import ReservaModal from './components/ReservaModal';
import MisViajesPage from './pages/MisViajesPage';
import PublicarAlojamientoPage from './pages/PublicarAlojamientoPage';
import AuditoriasPage from './pages/AuditoriasPage';
import { alojamientoService } from './services/alojamientoService';
import { reservaService } from './services/reservaService';
import axiosClient from './api/axiosClient';


export default function App({ keycloak }) {
  //Estado para alternar entre pantallas
    const [vistaActual, setVistaActual] = useState('home');
    const [alojamientos, setAlojamientos] = useState([]);
    const [cargandoAlojamientos, setCargandoAlojamientos] = useState(true);
    const [errorAlojamientos, setErrorAlojamientos] = useState(null);
    const [alojamientoSeleccionado, setAlojamientoSeleccionado] = useState(null);
    const [misReservas, setMisReservas] = useState([]);
    const [usuarioActual, setUsuarioActual] = useState(null);


    useEffect(() => {
      if (keycloak && keycloak.authenticated) {
        const roles = keycloak.tokenParsed?.realm_access?.roles || [];
        axiosClient.get('/usuarios/me').then((res) => {
          setUsuarioActual({
            idUsuario: res.data.id_usuario,
            nombre: keycloak.tokenParsed?.name || keycloak.tokenParsed?.preferred_username,
            email: keycloak.tokenParsed?.email,
            roles: roles,
          });
        });
      } else {
        setUsuarioActual(null);
      }
    }, [keycloak]);

    useEffect(() => {
        const fetchAlojamientos = async () => {
          try {
            setCargandoAlojamientos(true);
            const data = await alojamientoService.getAll();
            setAlojamientos(data);
            setErrorAlojamientos(null);
          } catch (err) {
            console.error('Error al cargar alojamientos desde la BD:', err);
            setErrorAlojamientos('No se pudieron obtener las propiedades.');
          } finally {
            setCargandoAlojamientos(false);
          }
        };
        fetchAlojamientos();
        }, []);

    useEffect(() => {
        const cargarReservas = async () => {
            if (!usuarioActual) {
                setMisReservas([]);
                return;
            }
            try {
                const response = await axiosClient.get(`/reservas/usuario/${usuarioActual.idUsuario}`);
                setMisReservas(response.data);
            } catch (err) {
                console.error('Error al cargar las reservas desde la BD:', err);
            }
        };
        if (vistaActual === 'viajes') {
            cargarReservas();
        }
    }, [vistaActual, usuarioActual]);

    const handleLogin = () => {
      if (keycloak) {
        keycloak.login();
      }
    };

    const handleLogout = () => {
      if (keycloak) {
        keycloak.logout({ redirectUri: window.location.origin });
      }
    };

    // Funciones para manejar la búsqueda y la reserva
    const handleBuscar = (filtros) => {
        if (!filtros.ciudad.trim()) {
            setAlojamientos(ALOJAMIENTOS_MOCK);
            return;
        }
        const filtrados = ALOJAMIENTOS_MOCK.filter((item) =>
            item.ciudad.toLowerCase().includes(filtros.ciudad.toLowerCase())
        );
        setAlojamientos(filtrados);
    };

    const handleAbrirModal = (alojamiento) => {
        if (!usuarioActual) {
          handleLogin();
          return;
        }
        setAlojamientoSeleccionado(alojamiento);
    };

    const handleConfirmarReserva = async (datosReserva) => {
                try {
            const data = await reservaService.confirmarReserva(datosReserva);
            const idGenerado = data?.idReserva;
            setAlojamientoSeleccionado(null);
            alert(`¡Reserva creada con éxito! ID de reserva: ${idGenerado || ''}`);
            setVistaActual('viajes');
        } catch (err) {
            console.error('Error al confirmar reserva en el backend:', err);
            const mensaje = err.response?.data?.message || err.response?.data || 'Error al procesar la reserva en el servidor.';
            alert(typeof mensaje === 'string' ? mensaje : JSON.stringify(mensaje));
            throw err;
        }
    };

    const handleCancelarReserva = async (idReserva) => {
        if (!window.confirm('¿Seguro que deseas cancelar esta reserva?')) {
            return;
        }
        try {
            await reservaService.cancelarReserva(idReserva);

                setMisReservas((prev) =>
                prev.map((r) =>
                    r.id_reserva === idReserva ? { ...r, estado_reserva: 'CANCELADA' } : r
                )
            );

            alert('La reserva fue cancelada exitosamente.');
        } catch (error) {
            console.error('Error al cancelar la reserva en el backend:', error);
            const mensaje =
                error.response?.data?.message ||
                error.response?.data ||
                'No se pudo cancelar la reserva.';
            alert(typeof mensaje === 'string' ? mensaje : JSON.stringify(mensaje));
        }
    };

    const handlePublicarExitoso = async (datosFormulario) => {
        try {
          const nuevoAlojamiento = await alojamientoService.save(datosFormulario);
          setAlojamientos((prev) => [nuevoAlojamiento, ...prev]);
          setVistaActual('home');
        } catch (err) {
          console.error('Error al publicar el alojamiento:', err);
          alert('Error al guardar la propiedad en la base de datos.');
        }
    };

    // Visual
  return (
    <div className="min-vh-100 bg-white d-flex flex-column">
      <Navbar
      vistaActual={vistaActual}
      setVistaActual={setVistaActual}
      usuarioActual={usuarioActual}
      onLogin={handleLogin}
      onLogout={handleLogout}
      />

      <main className="flex-grow-1">
        {vistaActual === 'home' && (
          <div>
            <HeroSearch onBuscar={handleBuscar} />

            {/* SECCIÓN MODIFICADA: Ahora contiene la fila y las tarjetas mapeadas */}
            <div className="container py-4">
              <h3 className="fw-bold mb-1">Alojamientos</h3>
              <p className="text-muted small mb-4">Descubre los mejores alojamientos para tu próxima aventura</p>

              {/* Fila responsiva de 4 columnas */}
              <div className="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-4">
                {alojamientos.map((item) => (
                  <div 
                    key={item.id_alojamiento ?? item.idAlojamiento ?? item.id} 
                    className="col"
                  >
                    <AlojamientoCard
                      alojamiento={item}
                      onReservarClick={handleAbrirModal}
                    />
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}

        {vistaActual === 'viajes' && (
        <MisViajesPage
            reservas={misReservas}
            onCancelarReserva={handleCancelarReserva}
            onVolverHome={() => setVistaActual('home')}
            />
        )}

        {vistaActual === 'publicar' && (
          <PublicarAlojamientoPage
            onPublicarExitoso={handlePublicarExitoso}
            onCancelar={() => setVistaActual('home')}
          />
        )}
          {vistaActual === 'auditorias' && (
              <AuditoriasPage
                  onVolverHome={() => setVistaActual('home')}
              />
          )}
      </main>

      {alojamientoSeleccionado && (
        <ReservaModal
          alojamiento={alojamientoSeleccionado}
          idUsuarioActual={usuarioActual?.idUsuario}
          onClose={() => setAlojamientoSeleccionado(null)}
          onConfirmarReserva={handleConfirmarReserva}
        />
      )}

      {/*Pie de pagina */}
      <footer className="border-top py-3 text-center text-muted small">
        © 2026 Mish - Proyecto Base de Datos
      </footer>
    </div>
  );
}