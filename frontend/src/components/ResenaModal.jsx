import React, { useState } from 'react';
import { resenaService } from '../services/resenaService';

export default function ResenaModal({ reserva, onClose, onResenaCreada }) {
    const [tituloResena, setTituloResena] = useState('');
    const [comentario, setComentario] = useState('');
    const [calificacion, setCalificacion] = useState(5);
    const [cargando, setCargando] = useState(false);
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setCargando(true);
        setError('');

        try {
            await resenaService.crearResena({
                idReserva: reserva.idReserva || reserva.id_reserva || reserva.id,
                tituloResena,
                comentario,
                calificacion: Number(calificacion),
            });

            alert('¡Reseña registrada con éxito!');
            if (onResenaCreada) onResenaCreada();
            onClose();
        } catch (err) {
            console.error('Error al guardar reseña:', err);
            const msg = err.response?.data?.mensaje || err.response?.data || 'Error al guardar la reseña.';
            setError(typeof msg === 'string' ? msg : JSON.stringify(msg));
        } finally {
            setCargando(false);
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
                        <h5 className="modal-title fw-bold">Calificar estadía</h5>
                        <button
                            type="button"
                            className="btn-close shadow-none"
                            onClick={onClose}
                            disabled={cargando}
                        ></button>
                    </div>

                    <form onSubmit={handleSubmit}>
                        <div className="modal-body">
                            <p className="text-muted small mb-3">
                                Tu opinión sobre <strong>{reserva.titulo || reserva.alojamiento_titulo}</strong>
                            </p>

                            {error && <div className="alert alert-danger py-2 small">{error}</div>}

                            {/* Selector de calificación (1-5) */}
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

                            {/* Título de la reseña */}
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

                            {/* Comentario */}
                            <div className="mb-3">
                                <label className="form-label small fw-semibold text-muted">Comentario</label>
                                <textarea
                                    className="form-control"
                                    rows="4"
                                    placeholder="Detalles sobre limpieza, comodidad y trato..."
                                    value={comentario}
                                    onChange={(e) => setComentario(e.target.value)}
                                    required
                                ></textarea>
                            </div>
                        </div>

                        <div className="modal-footer border-top-0 pt-0">
                            <button
                                type="button"
                                className="btn btn-light rounded-pill px-4"
                                onClick={onClose}
                                disabled={cargando}
                            >
                                Cancelar
                            </button>
                            <button
                                type="submit"
                                className="btn btn-primary rounded-pill px-4"
                                style={{ backgroundColor: '#1668e3' }}
                                disabled={cargando}
                            >
                                {cargando ? 'Publicando...' : 'Publicar reseña'}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}