package gradeAnalysis;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.*;
import java.util.*;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.OptionPaneUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import studentPackage.Student;
import studentPackage.StudentsDatabase;

public class SubjectPanel extends JPanel{
	JRadioButton[] b;
	String[] column = {"학번", "이름", "점수", "학점"};
	Object[][] rowData = new Object[studentDatabase.size()][column.length]; //테이블의 데이터배열;
	JTable table = new JTable(rowData,column); //JTable 추가
	JLabel sum = new JLabel();
	JLabel average = new JLabel();
	//불러온 점수의 합과 평균을 저장한다.
	JPanel labelPanel;
	JPanel buttonPanel;
	int[] totalSum;
	double[] totalAverage;
	JComboBox<String> sortbox; //콤보박스 초기화를 위해 빼놓음
	
	private static Vector<Student> studentDatabase = StudentsDatabase.getStudentsDatabase();//학생데이터베이스 정보를 가지고왔다
	
	public SubjectPanel() {
		//과목
		String[] subject = {"국어", "영어", "수학", "사회", "과학", "전체"};
		//panel1의 레이아웃
		setLayout(new BorderLayout());
		setBorder(new TitledBorder(new LineBorder(Color.GRAY,2),"과목별")); //테두리
		//RadioBoxChecked MyIL = new RadioBoxChecked();
		
		//과목선택- 라디오버튼 을 담을 패널 = p1
		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout());
		JLabel selectedSubject = new JLabel("과목선택");
		selectedSubject.setFont(new Font("한초롬돋움", Font.PLAIN, 30 ));
		p1.add(selectedSubject); //라디오 그릅 부착
		ButtonGroup subjects = new ButtonGroup();
		b = new JRadioButton[subject.length];
		
		
		//라디오 버튼 추가하기
		for(int i = 0; i< subject.length; i++) {
			b[i] = new JRadioButton(subject[i]);
			b[i].setFont(new Font("한초롬돋움", Font.PLAIN, 20));
			subjects.add(b[i]); //라디오그룹에 부착
			b[i].addItemListener(new RadioButtonListener());
			p1.add(b[i]); 
		}
		add(p1, BorderLayout.NORTH); //panel1의 상단에 위치
		
		
		//테이블과 p3패널을 담을 패널 = p2
		JPanel p2 = new JPanel();
		JPanel p5 = new JPanel(); //콤보박스를 붙이기 위한 패널
		p2.setLayout(new BorderLayout());
		p5.setLayout(new BorderLayout());
	
		
		//rowData배열에 studentDB의 속성값들을 저장한다.
		for (int i = 0; i < studentDatabase.size(); i++) {
			rowData[i][0] = studentDatabase.get(i).getStudentID();
			rowData[i][1] = studentDatabase.get(i).getName();
		}
		
		//셀크기 지정한다.
		TableColumnModel tcm = table.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(10); //학번셀 너비 지정
		tcm.getColumn(1).setPreferredWidth(100); //이름셀 너비 지정
		
		
		//테이블안 내용을 가운데 정렬하기 위해서 생성한다.
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcmSchedule = table.getColumnModel();
		for (int i = 0; i < tcmSchedule.getColumnCount(); i++) {

			tcmSchedule.getColumn(i).setCellRenderer(dtcr);
		}

		//테이블헤더 글씨크기
		table.getTableHeader().setFont(new Font("한초롬돋움", Font.BOLD, 20));
		table.setRowHeight(40); //모든 셀의 높이 지정
		table.setFont(new Font("함초롬돋움", Font.PLAIN, 20));//테이블내용글자 크기 변경
		JScrollPane scroll = new JScrollPane(table); //스크롤 바 달기
		p2.add(scroll,BorderLayout.CENTER); //p2패널의 가운데 테이블 추가	
		add(p2, BorderLayout.CENTER); //panel1의 가운데 위치
		
		//콤보박스 이용해서 정렬방법
		String[] sort = {"학번순","이름순","점수순"};
		sortbox = new JComboBox<String>(sort);
		sortbox.setFont(new Font("함초롬돋움", Font.BOLD, 20));
		JPanel p6 = new JPanel();
		p6.setLayout(new BorderLayout());
		p6.add(sortbox, BorderLayout.CENTER);
		p6.add(new JLabel("          "), BorderLayout.EAST);
		p5.add(p6, BorderLayout.EAST);
		p5.add(new JLabel("          "), BorderLayout.CENTER);
		//p5.setBackground(Color.green);
		p2.add(p5, BorderLayout.NORTH);
		sortbox.addItemListener(new CombBoxListener()); //리스너 추가
		
		
		//테이블 옆에 빈공간 넣기 위해 라벨 두개 붙임
		p2.add(new JLabel("          "),BorderLayout.WEST); 
		p2.add(new JLabel("          "),BorderLayout.EAST);
		
		//과목의 총합 과 과목의 평균 & 파일로 저장 버튼을 담는 패널 = p3
		JPanel labelButtonPanel = new JPanel();
		labelPanel = new JPanel(); //라벨을 붙이는 패널
		buttonPanel = new JPanel(); //버튼을 붙이는 패널
		JPanel p4 = new JPanel();
		labelButtonPanel.setLayout(new CardLayout());
		//labelButtonPanel.add(new JLabel(" "), BorderLayout.NORTH);
		//labelButtonPanel.add(new JLabel(" "), BorderLayout.SOUTH);
		labelPanel.setLayout(new GridLayout(2,2,5,5));
		buttonPanel.setLayout(new BorderLayout());
		labelButtonPanel.add(labelPanel);
		labelButtonPanel.add(buttonPanel);
		p4.setLayout(new BorderLayout());
		
		
		//과목의 총합
		JLabel subjectSum = new JLabel("과목의 총합",SwingConstants.CENTER);
		subjectSum.setFont(new Font("함초롬돋움",Font.BOLD, 17));
		sum.setFont(new Font("함초롬돋움",Font.BOLD, 15));
		labelPanel.add(subjectSum);
		sum.setOpaque(true);
		sum.setBackground(new Color(0xD9D9D9));
		labelPanel.add(sum);
		//labelPanel.add(new JLabel("")); //줄 맞추기위해 추가
		
		
		//과목의 평균
		JLabel subjectAverage = new JLabel("과목의 평균",SwingConstants.CENTER);
		subjectAverage.setFont(new Font("함초롬돋움",Font.BOLD, 17));
		average.setFont(new Font("함초롬돋움",Font.BOLD, 15));
		
		//버튼 이미지 (파일로 저장)
		ImageIcon originalIcon = new ImageIcon("images/store2.jpg");
		Image originImg = originalIcon.getImage(); 
		Image storeImage = originImg.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		ImageIcon storeIcon = new ImageIcon(storeImage);

		JButton fileStore = new JButton(storeIcon);
		fileStore.setBorderPainted(false);
		fileStore.setFocusPainted(false);
		fileStore.setContentAreaFilled(false);

		JLabel store = new JLabel("파일을 저장하려면 버튼을 눌러주세요!");
		//
		store.setFont(new Font("함초롬돋움",Font.BOLD, 15));
		store.setHorizontalAlignment(JLabel.RIGHT);

		//파일 저장하는 리스너를 추가한다.
		fileStore.addActionListener(new StoreFileListener());
		labelPanel.add(subjectAverage);
		average.setOpaque(true);
		average.setBackground(new Color(0xD9D9D9));
		labelPanel.add(average);
		buttonPanel.add(fileStore, BorderLayout.EAST);
		buttonPanel.add(store, BorderLayout.CENTER);
		p4.add(new JLabel("          "),BorderLayout.WEST);
		p4.add(new JLabel("          "),BorderLayout.EAST);
		p4.add(labelButtonPanel, BorderLayout.CENTER);
		p2.add(p4, BorderLayout.SOUTH); //p2패널의 하단에 p3 패널 추가
		
	}

	
	//라디오 버튼이 체크되었을 때 
	class RadioButtonListener implements ItemListener{
		//getSumSubject와 getAverageSubject 메소드가 int배열을 return함으로 배열로 받는다.
		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			//****과목을 다시 선택하면 정렬방식은 학점순이다.
			sortbox.setSelectedIndex(0);
			JRadioButton radiobutton = (JRadioButton)e.getSource();
			totalSum = CalculateGrade.getSumBySubject();
			totalAverage = CalculateGrade.getAverageBySubject();
			CalculateGrade.getSum_AverageByStudent();
			CalculateGrade.calculateGrade();
			
			if(radiobutton != b[5]) {
				labelPanel.setVisible(true);
				buttonPanel.setVisible(false);
			}
			else
				labelPanel.setVisible(false);
				buttonPanel.setVisible(true);
				
				
			if(radiobutton == b[0]) { //국어과목의 라디오버튼이 체크되었을 때
				for (int i = 0; i < studentDatabase.size(); i++) {
					rowData[i][2] = studentDatabase.get(i).koreanGrade;
					rowData[i][3] = studentDatabase.get(i).grade[0];	
					
				}
				sum.setText(Integer.toString(totalSum[0]));
				average.setText(Double.toString(totalAverage[0]));			
				GraphPanel.paintGraph(rowData,0);
				
			}
			else if(radiobutton == b[1]) {//영어과목의 라디오버튼이 체크되었을 때
				for (int i = 0; i < studentDatabase.size(); i++) {
					rowData[i][2] = studentDatabase.get(i).englishGrade;
					rowData[i][3] = studentDatabase.get(i).grade[1];
				}
				sum.setText(Integer.toString(totalSum[1]));
				average.setText(Double.toString(totalAverage[1]));
				GraphPanel.paintGraph(rowData ,1);
			}
			else if(radiobutton == b[2]) {//수학과목의 라디오버튼이 체크되었을 때
				for (int i = 0; i < studentDatabase.size(); i++) {
					rowData[i][2] = studentDatabase.get(i).mathGrade;
					rowData[i][3] = studentDatabase.get(i).grade[2];
				}
				sum.setText(Integer.toString(totalSum[2]));
				average.setText(Double.toString(totalAverage[2]));
				GraphPanel.paintGraph(rowData,2);

			}
			else if(radiobutton == b[3]) {//사회과목의 라디오버튼이 체크되었을 때
				for (int i = 0; i < studentDatabase.size(); i++) {
					rowData[i][2] = studentDatabase.get(i).societyGrade;
					rowData[i][3] = studentDatabase.get(i).grade[3];
				}
				sum.setText(Integer.toString(totalSum[3]));
				average.setText(Double.toString(totalAverage[3]));
				GraphPanel.paintGraph(rowData,3);

			}
			else if(radiobutton == b[4]) {//과학과목의 라디오버튼이 체크되었을 때
				for (int i = 0; i < studentDatabase.size(); i++) {
					rowData[i][2] = studentDatabase.get(i).scienceGrade;
					rowData[i][3] = studentDatabase.get(i).grade[4];
				}
				sum.setText(Integer.toString(totalSum[4]));
				average.setText(Double.toString(totalAverage[4]));
				GraphPanel.paintGraph(rowData,4);
			}
			else if(radiobutton == b[5]) {//전체 라디오버튼이 체크되었을 때
				for (int i = 0; i < studentDatabase.size(); i++) {
					rowData[i][2] = studentDatabase.get(i).average;
					rowData[i][3] = studentDatabase.get(i).totalGrade;
				}
				GraphPanel.paintGraph(rowData,5);
			}
			
			table.updateUI();
			
		}
		
	}
	
	//콤보박스 리스너
	//학번순 이름순 점수순으로 정렬한다.
	class CombBoxListener implements ItemListener{
	    
		@Override
		public void itemStateChanged(ItemEvent e) {
			JComboBox<String> combo = (JComboBox)e.getSource();
		    
			//현재 rowData 넘겨주기
			QuickSorter quickSorter = new QuickSorter(rowData);

			if(combo.getSelectedItem().equals("학번순")) {
				quickSorter.sort(0); //지금 선택한 정렬방법을 넘겨준다.
			}
			else if(combo.getSelectedItem().equals("이름순")) {
				quickSorter.sort(1);
			}
			else if(combo.getSelectedItem().equals("점수순")) {
				quickSorter.sort(2);

			}
			
			table.updateUI();
		}
		
	}
}
