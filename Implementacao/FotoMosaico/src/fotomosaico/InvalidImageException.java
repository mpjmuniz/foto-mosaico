package fotomosaico;

/**
 * Lançado para indicar que foi passado à um método uma referência nula
 * como imagem.
 * 
 * @author Marcelo Pablo
 */

public class InvalidImageException extends IllegalArgumentException{
	/**
     * Constrói uma <code>InvalidImageException</code> com uma mensagem
	 * de detalhe especificada.
	 * 
	 * @param   msg   the detail message.
     */
	public InvalidImageException(String msg){
		super(msg);
	}
}
