package gradeAnalysis;

import java.awt.event.*;
import java.util.*;

import javax.swing.JOptionPane;

import studentPackage.*;
import java.io.*;

public class StoreFileListener implements ActionListener {
	Vector<Student> db = StudentsDatabase.getStudentsDatabase();
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(null, "성적 파일(grade.dat)이 저장되었습니다.", "Message", JOptionPane.INFORMATION_MESSAGE);

		try {
			PrintWriter pw = new PrintWriter("grade.dat.txt");
			pw.println("학번 이름 국어 영어 수학 사회 과학 평균 학점");
			for (int i = 0; i < db.size(); i++) {
				Student s = db.get(i);
				pw.println(s.getStudentID()+" "+s.getName()+" "+s.koreanGrade+" "+s.englishGrade+" "+s.mathGrade+" "+s.societyGrade+" "+s.scienceGrade+" "+s.average+" "+s.totalGrade);
				
			}
			pw.close();
		} catch(IOException ex) {
			System.out.println("Error!");
		}
		
	}
}
