package fotomosaico;

import java.awt.image.BufferedImage;

/**
 *
 * A Classe <code>Candidato</code> modela um ladrilho candidato à 
 * representação de um pixel na imagem final.
 * 
 * <p>
 * Esta classe depende de objetos do tipo <code>BufferedImage</code>,
 * tipos primitivos <code>int</code> e <code>double</code>.
 *
 * @see BufferedImage
 * 
 * @author Marcelo Muniz
 */

class Candidato {
	BufferedImage imagem;
	int usos;
	double intensidadeMedia;
	
	/**
     * Constrói um <code>Candidato</code> de uma 
	 * <code>BufferedImage</code>, para a escrita no mosaico final.
	 * A partir da imagem passada, também se calcula a intensidade média da 
	 * imagem.
	 * 
	 * @param img Imagem para representação
     * 
     * @see InvalidImageException
     */
	
	public Candidato(BufferedImage img){
		if(img == null)	throw new InvalidImageException("Imagem inválida. Passe uma referência de BufferedImage para ser processada.");

		this.imagem = img;
		this.intensidadeMedia = Operacoes.calcularIntensidadeMedia(imagem);
		this.usos = 0;
	}
	
}
