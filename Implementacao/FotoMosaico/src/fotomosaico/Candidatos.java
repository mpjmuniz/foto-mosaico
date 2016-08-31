package fotomosaico;

/**
 * Implementação de um array escalonável de <code>Candidatos</code>.
 * Implementa todas as operações relativas à manipulação dos candidatos 
 * necessárias no decurso do programa. Não permite uso de imagens inválidas, ou 
 * seja, referências nulas de <code>BufferedImage</code>.
 * 
 * <p>Cada instância de lista de <tt>Candidatos</tt> tem uma 
 * <i>capacidade</i>. A capacidade é o tamanho do array utilizado para guardar 
 * os elementos na lista. Ao passo que os elementos são adicionados à lista, 
 * ela cresce em sua capacidade.
 * 
 * @author  Marcelo Pablo
 * @see     Candidato
 */

public class Candidatos {
	
	/**
     * A largura genérica de cada Candidato presente na lista.
     *
     */
	private int largura;
	
	/**
     * A altura genérica de cada Candidato presente na lista.
     *
     */
	private int altura;
	
	/**
     * O tamanho da lista de Candidatos (O número de elementos que ela contém).
     *
     */
	private int tamanho;
	
	/**
     * O array em que os elementos são armazenados.
     * A capacidade da lista de Candidatos é o atributo length deste array. 
     */
	private Candidato[] elems;
	
	/**
     * Constrói uma lista de Candidatos com um elemento dado como modelo.
     *
     * @param  modelo  o exemplo de que serão tirados os dados genéricos.
     * @throws IllegalArgumentException se a referência dada como modelo for 
	 * inválida
     */
	public Candidatos(Candidato modelo){
		if(modelo == null) throw new IllegalArgumentException("Referência inválida");
		
		elems = new Candidato[2];
		elems[0] = modelo;
		tamanho++;
		largura = modelo.imagem.getWidth();
		altura = modelo.imagem.getHeight();
	}
	
	/**
        * Insere o <code>Candidato</code> especificado na posição dada dentro da lista. 
        * Translada o elemento na posição especificada e os subsequentes (se 
        * houver algum).
        *
        * @param chave índice em que o elemento especificado será inserido.
        * @param c elemento a ser inserido.
        * @throws IndexOutOfBoundsException {@inheritDoc}
        * @throws IllegalArgumentException se a referência de Candidato for inválida.
        */
	public void inserir(Candidato c, int chave){
		if(chave < 0 || chave > tamanho) throw new ArrayIndexOutOfBoundsException("chave inválida");
		if(c == null) throw new IllegalArgumentException("Referência Inválida");
		
		if(tamanho() == capacidade()){
			Candidato[] novoElems = new Candidato[tamanho() * 2];
			System.arraycopy(elems, 0, novoElems, 0, tamanho());
			elems = novoElems;
		}
		
		System.arraycopy(elems, chave, elems, chave + 1, tamanho() - chave);
		elems[chave] = c;
		tamanho++;
	}
        
	/**
        * Ordena a lista de <code>Candidatos</code> conforme a intensidade média.
        * Implementação de Insertion Sort.
     */
	public void ordenar(){
		int j;
		Candidato temp;
		for (int i = 0; i < tamanho(); i++) {
			j = i;
			while(j > 0 && elems[j].intensidadeMedia < elems[j - 1].intensidadeMedia){
				temp = elems[j];
				elems[j] = elems[j-1];
				elems[j-1] = temp;
				j = j - 1;
			}
		}
	}
	
	/**
     * Remove o elemento da posição especificada de dentro da lista.
     * Translada o elemento na posição especificada e os subsequentes (se 
	 * houver algum).
     *
     * @param chave indice do elemento a ser removido
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
	public void remover(int chave){
		if(chave < 0 || chave >= tamanho) throw new ArrayIndexOutOfBoundsException("chave inválida");
		
		System.arraycopy(elems, chave + 1, elems, chave, tamanho() - (chave + 1));
		tamanho--;
	}
	
	/**
     * Retorna o elemento na posição especificada dentro da lista.
     *
     * @param  chave índice do elemento à retornar.
     * @return O Candidato na posição especificada
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
	public Candidato consultar(int chave){
		if(chave < 0 || chave >= tamanho) throw new ArrayIndexOutOfBoundsException("chave inválida");
		
		return elems[chave];
	}
	
	/**
     * Retorna o elemento adequado para a intensidade e o limiar passado.
     * Implementa busca binária para retornar o elemento mais adequado.
	 * 
     * @param  intensidade índice do elemento à retornar.
	 * @param  epsilon índice do elemento à retornar.
     * @return O Candidato na posição especificada
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public Candidato buscaBinaria(double intensidade, double epsilon){
            int inicio = 0, fim = this.tamanho() - 1, meio;
            while(inicio <= fim){
                meio = (inicio + fim)/2;
				
                if(Math.abs(intensidade - elems[meio].intensidadeMedia) <= epsilon ){
                    if(elems[meio].usos <= 10){
                        return elems[meio];
                    } else {
                        for (int i = meio - 100; i < meio + 100 && (i >= 0 && i < tamanho() ); i++) {
                            if(Math.abs(intensidade - elems[i].intensidadeMedia) <= epsilon && elems[i].usos <= 10) return elems[i];
                        }
                    }
                }
                
                if(elems[meio].intensidadeMedia > intensidade){
                    fim = meio - 1;
                } else {
                    inicio = meio + 1;
		}
            }
            return elems[0];
        }
        
	/**
     * Retorna o número de elementos na lista.
     *
     * @return o número de elementos na lista
     */
	public int tamanho(){
		return this.tamanho;
	}
	
	/**
     * Retorna o tamanho do array interno de armazenamento.
     *
     * @return o tamanho do array interno de armazenamento.
     */
	public int capacidade(){
		return this.elems.length;
	}
	
	/**
     * Retorna a largura genérica dos Candidatos na lista.
     *
     * @return a largura genérica dos Candidatos na lista.
     */
	public int largura(){
		return this.largura;
	}
	
	/**
     * Retorna a altura genérica dos Candidatos na lista.
     *
     * @return a altura genérica dos Candidatos na lista.
     */
	public int altura(){
		return this.altura;
	}
}
