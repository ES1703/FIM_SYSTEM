use SKL_LOAN

SELECT distinct A1.LoanKey LoanKey,A1.CL_MainUKey CL_MainUKey,A1.CityCode CityCode,A1.AreaCode AreaCode,A1.LandArea LandArea,A1.UseArea UseArea,A1.UseType UseType,A1.LandValue LandValue,
                A1.AplraiseUnitValue AplraiseUnitValue,A1.LandHoldArea LandHoldArea,
				A2.BuildPurpose BuildPurpose,A2.BuildMaterial BuildMaterial,A2.BuildType BuildType,A2.BuildFloor BuildFloor,A2.BuildLayer BuildLayer,A2.BuildLayerArea BuildLayerArea,
				A2.AplraiseUnitValue BuildAplraiseUnitValue,A2.RoofMaterial RoofMaterial,A2.BuildComDate BuildComDate,A2.AdjBuildArea AdjBuildArea,A2.AdjBuildChar AdjBuildChar,A2.AdjBuildCharOther AdjBuildCharOther,
				A3.DebtPerc DebtPerc,A3.AplraiseValue AplraiseValue,A3.ApprDate
INTO MyTable
FROM CL_ImmLand A1,CL_ImmBuild A2, CL_ImmMain A3
WHERE A3.UKey = A2.CL_MainUKey and A3.UKey = A1.CL_MainUKey and A3.LoanKey= A2.LoanKey and A3.LoanKey = A1.LoanKey
AND A3.ApprDate <'2020-07-01'
ORDER BY LoanKey

select CityCode,AreaCode,AreaItem 
into AreaCode
from Paras_Adm_Area

select CityCode,CityItem 
into CityCode
from Paras_Adm_City


select LoanKey,CL_MainUKey,CarParkArea,Price 
into CAR
from CL_ImmCar

Select * from MyTable 
LEFT JOIN AreaCode on MyTable.CityCode = AreaCode.CityCode and MyTable.AreaCode = AreaCode.AreaCode
LEFT JOIN CityCode on MyTable.CityCode = CityCode.CityCode 
LEFT JOIN CAR on MyTable.LoanKey = CAR.LoanKey and MyTable.CL_MainUKey = CAR.CL_MainUKey

drop table CityCode,AreaCode,MyTable,CAR

