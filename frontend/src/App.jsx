import React, { useState, useEffect } from 'react';
import Navbar from './components/Navbar';
import HeroSearch from './components/HeroSearch';
import AlojamientoCard from './components/AlojamientoCard';
import ReservaModal from './components/ReservaModal';
import MisViajesPage from './pages/MisViajesPage';
import PublicarAlojamientoPage from './pages/PublicarAlojamientoPage';


export default function App({ keycloak }) {
  // Estado para alternar entre pantallas ('home', 'viajes', 'publicar')
    const [vistaActual, setVistaActual] = useState('home');
    const [alojamientos, setAlojamientos] = useState(ALOJAMIENTOS_MOCK);
    const [alojamientoSeleccionado, setAlojamientoSeleccionado] = useState(null);
    const [misReservas, setMisReservas] = useState([]);
    const [usuarioActual, setUsuarioActual] = useState(null);


    useEffect(() => {
    if (keycloak && keycloak.authenticated) {
      const roles = keycloak.tokenParsed?.realm_access?.roles || [];
      setUsuarioActual({
        id: keycloak.tokenParsed?.sub,
        nombre: keycloak.tokenParsed?.name || keycloak.tokenParsed?.preferred_username,
        email: keycloak.tokenParsed?.email,
        roles: roles,
      });
      } else {
        setUsuarioActual(null);
      }
    }, [keycloak]);

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

    const handleConfirmarReserva = (datosReserva) => {
        const nuevaReserva = {
        id: Date.now(), // Simula ID auto-generado
        ...datosReserva,
        alojamiento_titulo: alojamientoSeleccionado.titulo,
        alojamiento_ciudad: alojamientoSeleccionado.ciudad,
        alojamiento_imagen: alojamientoSeleccionado.url_imagen,
        estado: 'CONFIRMADA',
        fecha_creacion: new Date().toLocaleDateString(),
        };

        setMisReservas([nuevaReserva, ...misReservas]);
        setAlojamientoSeleccionado(null);
        alert('¡Reserva creada con éxito! Redirigiendo a Mis Viajes...');
        setVistaActual('viajes');
    };

    const handleCancelarReserva = (idReserva) => {
        if (window.confirm('¿Seguro que deseas cancelar esta reserva?')) {
        // Actualiza el estado en memoria (luego será PUT al backend)
            setMisReservas(
                misReservas.map((r) =>
                    r.id === idReserva ? { ...r, estado: 'CANCELADA' } : r
                )
            );
        }
    };

    const handlePublicarExitoso = (nuevoAlojamiento) => {
      const alojamientoCreado = {
        id: Date.now(), // ID temporal local
        ...nuevoAlojamiento,
      };

      // se agrega el nuevo alojamiento al inicio de la lista
      setAlojamientos([alojamientoCreado, ...alojamientos]);
      alert('¡Propiedad publicada exitosamente!');
      setVistaActual('home');
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
              <h3 className="fw-bold mb-1">Early booking deals</h3>
              <p className="text-muted small mb-4">Book early and save big on select stays</p>

              {/* Fila responsiva de 4 columnas */}
              <div className="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-4">
                {alojamientos.map((item) => (
                  <div key={item.id} className="col">
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
      </main>

      {/* Renderizado condicional del modal */}
      {alojamientoSeleccionado && (
        <ReservaModal
          alojamiento={alojamientoSeleccionado}
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

const ALOJAMIENTOS_MOCK = [
  {
    id: 1,
    titulo: 'Chalet in Lo Barnechea',
    tipo_alojamiento: 'Chalet',
    ciudad: 'Santiago',
    direccion: 'Camino Farellones 1420',
    capacidad: 8,
    estadia_minima: 2,
    precio_noche: 149000,
    politica_cancel: 'Flexible',
    url_imagen: 'https://images.unsplash.com/photo-1512917774080-9991f1c4c750?auto=format&fit=crop&w=600&q=80'
  },
  {
    id: 2,
    titulo: 'Apartment in Buenos Aires',
    tipo_alojamiento: 'Departamento',
    ciudad: 'Buenos Aires',
    direccion: 'Av. Santa Fe 3200',
    capacidad: 2,
    estadia_minima: 1,
    precio_noche: 45000,
    politica_cancel: 'Moderada',
    url_imagen: 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&w=600&q=80'
  },
  {
    id: 3,
    titulo: 'Apartment in Vina Del Mar',
    tipo_alojamiento: 'Departamento',
    ciudad: 'Viña del Mar',
    direccion: 'Av. San Martín 850',
    capacidad: 3,
    estadia_minima: 2,
    precio_noche: 52000,
    politica_cancel: 'Flexible',
    url_imagen: 'https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?auto=format&fit=crop&w=600&q=80'
  },
  {
    id: 4,
    titulo: 'House in Casablanca',
    tipo_alojamiento: 'Casa',
    ciudad: 'Casablanca',
    direccion: 'Ruta 68 Km 75',
    capacidad: 6,
    estadia_minima: 3,
    precio_noche: 85000,
    politica_cancel: 'Estricta',
    url_imagen: 'https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=600&q=80'
  },
  {
    id: 5,
    titulo: 'Cabaña rústica junto al lago',
    tipo_alojamiento: 'Cabaña',
    ciudad: 'Pucón',
    direccion: 'Camino al Volcán 2100',
    capacidad: 5,
    estadia_minima: 2,
    precio_noche: 75000,
    politica_cancel: 'Flexible',
    url_imagen: 'https://images.unsplash.com/photo-1470770841072-f978cf4d019e?auto=format&fit=crop&w=600&q=80'
  },
  {
    id: 6,
    titulo: 'Loft moderno en Lastarria',
    tipo_alojamiento: 'Loft',
    ciudad: 'Santiago',
    direccion: 'José Victorino Lastarria 180',
    capacidad: 2,
    estadia_minima: 1,
    precio_noche: 42000,
    politica_cancel: 'Flexible',
    url_imagen: 'https://images.unsplash.com/photo-1505691938895-1758d7feb511?auto=format&fit=crop&w=600&q=80'
  },
  {
    id: 7,
    titulo: 'Refugio en la montaña con tinaja',
    tipo_alojamiento: 'Cabaña',
    ciudad: 'Cajón del Maipo',
    direccion: 'Camino El Volcán 8450',
    capacidad: 4,
    estadia_minima: 2,
    precio_noche: 95000,
    politica_cancel: 'Moderada',
    url_imagen: 'https://images.unsplash.com/photo-1518780664697-55e3ad937233?auto=format&fit=crop&w=600&q=80'
  },
  {
    id: 8,
    titulo: 'Estudio frente a Playa Mansa',
    tipo_alojamiento: 'Departamento',
    ciudad: 'Punta del Este',
    direccion: 'Rambla Claudio Williman Parada 5',
    capacidad: 2,
    estadia_minima: 3,
    precio_noche: 110000,
    politica_cancel: 'Estricta',
    url_imagen: 'https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?auto=format&fit=crop&w=600&q=80'
  }
];