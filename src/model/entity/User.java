package model.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="User")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;	
	private String nome;
	private int site;
	private Date firstLogin;
	private Date lastLogin;

	//Modes
	private int modeBasicUsedCount;
	private int modeAdvancedUsedCount;
	private int modeProgrammerUsedCount;
	private int manualBetUsedCount;
	
	//Advanced Modes
	private int martingaleCount;
	private int labouchereCount;
	private int fibonacciCount;
	private int dalembertCount;
	private int presetListCount;
	
	
	@OneToMany(targetEntity=Bets.class,cascade = CascadeType.ALL)
	private List<Bets> userBets = new ArrayList<Bets>();
	
	
	public User(){}
	
	public User(String nome){
		setNome(nome);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getSite() {
		return site;
	}

	public void setSite(int site) {
		this.site = site;
	}

	public Date getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(Date firstLogin) {
		this.firstLogin = firstLogin;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public int getModeBasicUsedCount() {
		return modeBasicUsedCount;
	}

	public void setModeBasicUsedCount(int modeBasicUsedCount) {
		this.modeBasicUsedCount = modeBasicUsedCount;
	}

	public int getModeAdvancedUsedCount() {
		return modeAdvancedUsedCount;
	}

	public void setModeAdvancedUsedCount(int modeAdvancedUsedCount) {
		this.modeAdvancedUsedCount = modeAdvancedUsedCount;
	}

	public int getModeProgrammerUsedCount() {
		return modeProgrammerUsedCount;
	}

	public void setModeProgrammerUsedCount(int modeProgrammerUsedCount) {
		this.modeProgrammerUsedCount = modeProgrammerUsedCount;
	}

	public int getManualBetUsedCount() {
		return manualBetUsedCount;
	}

	public void setManualBetUsedCount(int manualBetUsedCount) {
		this.manualBetUsedCount = manualBetUsedCount;
	}

	public int getMartingaleCount() {
		return martingaleCount;
	}

	public void setMartingaleCount(int martingaleCount) {
		this.martingaleCount = martingaleCount;
	}

	public int getLabouchereCount() {
		return labouchereCount;
	}

	public void setLabouchereCount(int labouchereCount) {
		this.labouchereCount = labouchereCount;
	}

	public int getFibonacciCount() {
		return fibonacciCount;
	}

	public void setFibonacciCount(int fibonacciCount) {
		this.fibonacciCount = fibonacciCount;
	}

	public int getDalembertCount() {
		return dalembertCount;
	}

	public void setDalembertCount(int dalembertCount) {
		this.dalembertCount = dalembertCount;
	}

	public int getPresetListCount() {
		return presetListCount;
	}

	public void setPresetListCount(int presetListCount) {
		this.presetListCount = presetListCount;
	}

	public List<Bets> getUserBets() {
		return userBets;
	}

	public void setUserBets(Bets bet) {
		this.userBets.add(bet);
	}
	
}
