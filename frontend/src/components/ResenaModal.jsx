import React, { useState, useEffect } from 'react';
import { resenaService } from '../services/resenaService';

export default function ResenaModal({ reserva, onClose, onResenaCreada }) {
    const [resenaExistente, setResenaExistente] = useState(null);
    const [cargandoInfo, setCargandoInfo] = useState(true);

    // Estados del formulario
    const [tituloResena, setTituloResena] = useState('');
    const [comentario, setComentario] = useState('');
    const [calificacion, setCalificacion] = useState(5);
    const [enviando, setEnviando] = useState(false);
    const [error, setError] = useState('');

    const idReserva = reserva?.id_reserva || reserva?.idReserva || reserva?.id;

    // Cargar reseña existente si la hay
    useEffect(() => {
        const cargarResena = async () => {
            try {
                setCargandoInfo(true);
                const data = await resenaService.obtenerPorReserva(idReserva);
                if (data && (data.id_resena || data.calificacion)) {
                    setResenaExistente(data);
                }
            } catch (err) {
                console.error('Error al consultar reseña previa:', err);
            } finally {
                setCargandoInfo(false);
            }
        };

        if (idReserva) {
            cargarResena();
        }
    }, [idReserva]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setEnviando(true);
        setError('');

        try {
            await resenaService.crearResena({
                idReserva: Number(idReserva),
                tituloResena,
                comentario,
                calificacion: Number(calificacion),
            });

            alert('¡Reseña registrada con éxito!');
            if (onResenaCreada) onResenaCreada();
            onClose();
        } catch (err) {
            const msg = err.response?.data?.mensaje || err.response?.data || 'Error al guardar la reseña.';
            setError(typeof msg === 'string' ? msg : JSON.stringify(msg));
        } finally {
            setEnviando(false);
        }
    };

    return (
        <div
            className="modal show d-block"
            tabIndex="-1"
            style={{ backgroundColor: 'rgba(0,0,0,0.5)', zIndex: 1060 }}
        >
            <div className="modal-dialog modal-dialog-centered">
                <div className="modal-content rounded-4 border-0 shadow">
                    <div className="modal-header border-bottom-0 pb-0">
                        <h5 className="modal-title fw-bold">
                            {resenaExistente ? 'Tu reseña' : 'Calificar estadía'}
                        </h5>
                        <button
                            type="button"
                            className="btn-close shadow-none"
                            onClick={onClose}
                            disabled={enviando}
                        ></button>
                    </div>

                    <div className="modal-body">
                        <p className="text-muted small mb-3">
                            Estadía en <strong>{reserva.titulo || reserva.alojamiento_titulo || 'Alojamiento'}</strong>
                        </p>

                        {cargandoInfo ? (
                            <div className="text-center py-4 text-muted small">Cargando detalles...</div>
                        ) : resenaExistente ? (
                            /* --- MODO VISTA: CUANDO YA EXISTE LA RESEÑA --- */
                            <div>
                                <div className="text-center mb-3">
                                    <div className="fs-3 text-warning">
                                        {'★'.repeat(resenaExistente.calificacion)}
                                        <span className="text-muted opacity-25">
                      {'★'.repeat(5 - resenaExistente.calificacion)}
                    </span>
                                    </div>
                                    <span className="text-muted small">
                    Puntuación: {resenaExistente.calificacion}/5
                  </span>
                                </div>

                                <div className="bg-light p-3 rounded-3 mb-3 border">
                                    <h6 className="fw-bold text-dark mb-1">{resenaExistente.titulo_resena}</h6>
                                    <p className="text-secondary small mb-2">{resenaExistente.comentario}</p>
                                    {resenaExistente.fecha_resena && (
                                        <span className="text-muted" style={{ fontSize: '0.75rem' }}>
                      Publicada el: {resenaExistente.fecha_resena}
                    </span>
                                    )}
                                </div>
                            </div>
                        ) : (
                            /* --- MODO FORMULARIO: CUANDO AÚN NO EXISTE --- */
                            <form onSubmit={handleSubmit}>
                                {error && <div className="alert alert-danger py-2 small">{error}</div>}

                                {/* Estrellas interactivas */}
                                <div className="mb-3 text-center">
                                    <label className="form-label d-block small fw-semibold text-muted">Puntuación</label>
                                    <div className="d-flex justify-content-center gap-2">
                                        {[1, 2, 3, 4, 5].map((star) => (
                                            <button
                                                type="button"
                                                key={star}
                                                className="btn p-0 border-0 fs-3"
                                                style={{ color: star <= calificacion ? '#ffc107' : '#e4e5e9' }}
                                                onClick={() => setCalificacion(star)}
                                            >
                                                ★
                                            </button>
                                        ))}
                                    </div>
                                </div>

                                <div className="mb-3">
                                    <label className="form-label small fw-semibold text-muted">Título</label>
                                    <input
                                        type="text"
                                        className="form-control"
                                        placeholder="Ej: Excelente lugar y ubicación"
                                        value={tituloResena}
                                        onChange={(e) => setTituloResena(e.target.value)}
                                        required
                                    />
                                </div>

                                <div className="mb-3">
                                    <label className="form-label small fw-semibold text-muted">Comentario</label>
                                    <textarea
                                        className="form-control"
                                        rows="4"
                                        placeholder="Describe los detalles de tu estadía..."
                                        value={comentario}
                                        onChange={(e) => setComentario(e.target.value)}
                                        required
                                    ></textarea>
                                </div>

                                <div className="d-flex justify-content-end gap-2 pt-2 border-top">
                                    <button
                                        type="button"
                                        className="btn btn-light rounded-pill px-4"
                                        onClick={onClose}
                                        disabled={enviando}
                                    >
                                        Cancelar
                                    </button>
                                    <button
                                        type="submit"
                                        className="btn btn-primary rounded-pill px-4"
                                        style={{ backgroundColor: '#1668e3' }}
                                        disabled={enviando}
                                    >
                                        {enviando ? 'Guardando...' : 'Publicar reseña'}
                                    </button>
                                </div>
                            </form>
                        )}
                    </div>

                    {/* Pie de modal para modo lectura */}
                    {resenaExistente && (
                        <div className="modal-footer border-top-0 pt-0">
                            <button type="button" className="btn btn-secondary rounded-pill px-4" onClick={onClose}>
                                Cerrar
                            </button>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}